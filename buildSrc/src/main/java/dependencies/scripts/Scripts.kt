package dependencies.scripts

object Scripts {
    /**
     * Scripts Paths
     */
    const val GIT_VERSION_PATH = "config/gitversion/script-git-version.gradle"
    const val DETEKT_PATH = "config/detekt/detekt.gradle"
    const val JACOCO_PATH = "config/jacoco/jacoco.gradle"
    const val JACOCO_CONFIG_PATH = "config/jacoco/jacoco-config.gradle"
    const val JACOCO_VERSION = "0.8.12"
    const val SONAR_PATH = "config/sonar/sonar.gradle"

    /**
     * Reports Paths
     */
    const val DETEKT_REPORT_PATH = "reports/detekt/merge.xml"
    const val JACOCO_STAGING_UNIFIED_REPORT_PATH = "reports/jacoco/jacocoTestReport-staging-unified/jacocoTestReport-staging-unified.xml"
    const val JACOCO_STAGING_REPORT_PATH = "reports/jacoco/jacocoTestReportStaging/jacocoTestReportStaging.xml"
}
