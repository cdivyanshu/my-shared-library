package org.Optree.jenkins.stages

class Dast {
    static void call(Map params) {
        stage('Install ZAP') {
            steps {
                sh 'wget https://github.com/zaproxy/zaproxy/releases/download/v2.14.0/ZAP_2.14.0_Linux.tar.gz'
                sh 'tar -xvf ZAP_2.14.0_Linux.tar.gz'
            }
        }

        stage('Run ZAP Scan') {
            steps {
                sh "./ZAP_2.14.0/zap.sh -cmd -port ${params.ZAP_PORT} -quickurl ${params.ZAP_URL} -quickprogress -quickout zap-scan-report.html"
            }
        }

        stage('Publish ZAP Scan Report') {
            steps {
                publishHTML([
                    allowMissing: false,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: './',
                    reportFiles: 'zap-scan-report.html',
                    reportName: 'ZAP Scan Report',
                    reportTitles: ''
                ])
            }
        }

        stage('Archive Report') {
            steps {
                archiveArtifacts artifacts: 'unit-test-results.xml', allowEmptyArchive: true

                echo "Publishing Reports"
                dependencyCheckPublisher pattern: 'dependency-check-report.xml'
            }
        }
    }
}
