package org.yourorganization.jenkins.stages

def call(Map params) {
    pipeline {
        agent any

        environment {
            SCANNER_HOME = tool "${params.SCANNER_TOOL_NAME}"
        }

        stages {
            stage('SonarQube Analysis') {
                steps {
                    withSonarQubeEnv("${params.SONARQUBE_ENV_NAME}") {
                        sh "${SCANNER_HOME}/bin/sonar-scanner"
                    }
                }
            }
        }

        post {
            success {
                echo 'Static code analysis successful!'
            }

            failure {
                echo 'Static code analysis failed!'
            }
        }
    }
}
