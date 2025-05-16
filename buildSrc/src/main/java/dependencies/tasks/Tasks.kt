package dependencies.tasks

object Tasks {
    const val COLLECT_JACOCO_REPORTS = "collectJacocoReports"
    const val COLLECT_JACOCO_REPORTS_GROUP = "verification"
    const val COLLECT_JACOCO_REPORTS_DESCRIPTION = "Collects Jacoco reports from all subprojects and configures them for Sonarqube analysis"
    const val JACOCO_TEST_REPORT_STAGING = "jacocoTestReportStaging"
    const val TEST_STAGING_DEBUG_UNIT_TEST = "testStagingDebugUnitTest"
    const val SONAR_QUBE_COVERAGE_PROPERTY = "sonar.coverage.jacoco.xmlReportPaths"
    const val PASSED = "passed"
    const val SKIPPED = "skipped"
    const val FAILED = "failed"
}
