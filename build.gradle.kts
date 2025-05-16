import dependencies.android.Android.ANDROID_APPLICATION
import dependencies.android.Android.ANDROID_LIBRARY
import dependencies.scripts.Scripts.DETEKT_PATH
import dependencies.scripts.Scripts.DETEKT_REPORT_PATH
import dependencies.scripts.Scripts.JACOCO_PATH
import dependencies.scripts.Scripts.JACOCO_REPORT_PATH
import dependencies.scripts.Scripts.SONAR_PATH
import dependencies.tasks.Tasks.COLLECT_JACOCO_REPORTS
import dependencies.tasks.Tasks.COLLECT_JACOCO_REPORTS_DESCRIPTION
import dependencies.tasks.Tasks.COLLECT_JACOCO_REPORTS_GROUP
import dependencies.tasks.Tasks.FAILED
import dependencies.tasks.Tasks.JACOCO_TEST_REPORT_STAGING
import dependencies.tasks.Tasks.PASSED
import dependencies.tasks.Tasks.SKIPPED
import dependencies.tasks.Tasks.SONAR_QUBE_COVERAGE_PROPERTY
import dependencies.tasks.Tasks.TEST_STAGING_DEBUG_UNIT_TEST
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektPlugin
import io.gitlab.arturbosch.detekt.report.ReportMergeTask
import org.sonarqube.gradle.SonarTask

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.detekt.plugin)
    alias(libs.plugins.sonarqube.plugin)
    alias(libs.plugins.firebase.plugin)
    alias(libs.plugins.firebase.app.distribuiton.plugin) apply false
    jacoco
}



allprojects {
    afterEvaluate {
        /**
         * Verificar se é um módulo Android antes de aplicar as configurações
         */
        if (plugins.hasPlugin(ANDROID_APPLICATION) ||
            plugins.hasPlugin(ANDROID_LIBRARY)) {

            /**
             * Aplicar Detekt
             */
            apply(from = "${rootDir}/$DETEKT_PATH")

            /**
             * Aplicar JaCoCo
             */
            apply(from = "${rootDir}/$JACOCO_PATH")

            /**
             * Aplicar Sonar
             */
            apply("${rootDir}/$SONAR_PATH")
        }
    }

    /**
     * Detekt task para gerar reports
     */
    val reportMerge by tasks.registering(ReportMergeTask::class) {
        output.set(rootProject.layout.buildDirectory.file(DETEKT_REPORT_PATH))
    }

    plugins.withType(DetektPlugin::class) {
        tasks.withType(Detekt::class) detekt@{
            finalizedBy(reportMerge)

            reportMerge.configure {
                input.from(this@detekt.xmlReportFile)
            }
        }
    }

    tasks.withType<Test> {
        testLogging {
            events(PASSED, SKIPPED, FAILED)
        }
    }

    tasks.withType<JacocoReport> {
        dependsOn(TEST_STAGING_DEBUG_UNIT_TEST)
        reports {
            xml.required.set(true)
            html.required.set(true)
        }
    }

    tasks.register(COLLECT_JACOCO_REPORTS) {
        group = COLLECT_JACOCO_REPORTS_GROUP
        description = COLLECT_JACOCO_REPORTS_DESCRIPTION
        dependsOn(subprojects.map { it.tasks.named(JACOCO_TEST_REPORT_STAGING) })
        doLast {
            val reportPaths = subprojects.map { subproject ->
                "${subproject.buildDir}/$JACOCO_REPORT_PATH"
            }.filter { File(it).exists() }

            if (reportPaths.isNotEmpty()) {
                sonarqube {
                    properties {
                        property(SONAR_QUBE_COVERAGE_PROPERTY, reportPaths.joinToString(","))
                    }
                }
            }
        }
    }

    tasks.withType<SonarTask> {
        dependsOn(COLLECT_JACOCO_REPORTS)
    }
}