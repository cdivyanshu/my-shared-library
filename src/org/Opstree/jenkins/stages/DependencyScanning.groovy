package org.Opstree.jenkins.stages

def call() {
    pipeline {
        agent any

        stages {
            stage('Dependency Scan') {
                steps {
                    dependencyCheck additionalArguments: '--scan . --format ALL', odcInstallation: 'OWASP Dependency-Check Vulnerabilities'
                }
            }
        }

        post {
            success {
                echo 'Dependency scanning successful!'
            }

            failure {
                echo 'Dependency scanning failed!'
            }
        }
    }
}
