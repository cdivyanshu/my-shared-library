package org.Opstree.jenkins.stages

def call(Map params) {
    sh '''
        echo "Archiving reports..."
        archiveArtifacts artifacts: 'unit-test-results.xml', allowEmptyArchive: true

        echo "Publishing Reports"
        publishHTML([
            allowMissing: false,
            alwaysLinkToLastBuild: true,
            keepAll: true,
            reportDir: '.',
            reportFiles: 'dependency-check-report.html',
            reportName: 'Dependency Check Report',
            reportTitles: ''
        ])
    '''
}
