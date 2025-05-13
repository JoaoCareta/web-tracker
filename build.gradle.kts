import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektPlugin
import io.gitlab.arturbosch.detekt.report.ReportMergeTask

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.detekt.plugin)
    alias(libs.plugins.sonarqube.plugin)
    jacoco
}



allprojects {

    afterEvaluate {
        /**
         * Verificar se é um módulo Android antes de aplicar as configurações
         */
        if (plugins.hasPlugin("com.android.application") ||
            plugins.hasPlugin("com.android.library")) {

            /**
             * Aplicar Detekt
             */
            apply(from = "${rootDir}/config/detekt/detekt.gradle")

            /**
             * Aplicar Jacoco
             */
            apply(from = "${rootDir}/config/jacoco/jacoco.gradle")

            apply("${rootDir}/config/sonar/sonar.gradle")
        }
    }

    /**
     * Detekt task para gerar reports
     */
    val reportMerge by tasks.registering(ReportMergeTask::class) {
        output.set(rootProject.layout.buildDirectory.file("reports/detekt/merge.xml"))
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
            events("passed", "skipped", "failed")
        }
    }

    tasks.withType<JacocoReport> {
        dependsOn("testDebugUnitTest")
        reports {
            xml.required.set(true)
            html.required.set(true)
        }
    }
}