package org.Opstree.jenkins.stages

def call() {
    pipeline {
        agent any

        stages {
            stage('Unit Test') {
                steps {
                    sh 'go test ./... -v | tee unit-test-results.xml'
                }
            }
        }

        post {
            success {
                echo 'Bugs analysis successful!'
            }

            failure {
                echo 'Bugs analysis failed!'
            }
        }
    }
}
