package dependencies.scripts

object Scripts {
    /**
     * Scripts Paths
     */
    const val GIT_VERSION_PATH = "config/gitversion/script-git-version.gradle"
    const val DETEKT_PATH = "config/detekt/detekt.gradle"
    const val JACOCO_PATH = "config/jacoco/jacoco.gradle"
    const val SONAR_PATH = "config/sonar/sonar.gradle"

    /**
     * Reports Paths
     */
    const val DETEKT_REPORT_PATH = "reports/detekt/merge.xml"
    const val JACOCO_REPORT_PATH = "reports/jacoco/jacocoTestReportStaging/jacocoTestReportStaging.xml"
}
