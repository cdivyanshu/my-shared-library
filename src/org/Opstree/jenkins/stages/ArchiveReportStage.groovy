package org.Opstree.jenkins.stages

class ArchiveReportStage {
    static void stage() {
        stage('Archive Report') {
            steps {
                // Archive the unit test results
                archiveArtifacts artifacts: 'unit-test-results.xml', allowEmptyArchive: true

                echo "Publishing Reports"
                // Publish OWASP Dependency-Check report in HTML format
                publishHTML([
                    allowMissing: false,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: '.',
                    reportFiles: 'dependency-check-report.html',
                    reportName: 'Dependency Check Report',
                    reportTitles: ''
                ])
            }
        }
    }
}
