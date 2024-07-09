package org.Opstree.jenkins.stages

def call(Map params) {
    pipeline {
        agent any

        environment {
            GO_VERSION = '1.18'
        }

        stages {
            stage('Checkout') {
                steps {
                    git branch: "${params.GIT_BRANCH}", url: "${params.GIT_URL}"
                }
            }

            stage('Modify go.mod') {
                steps {
                    sh '''
                    export GO111MODULE=on
                    sed -i 's/go 1.20/go 1.18/g' go.mod
                    '''
                }
            }

            stage('Build') {
                steps {
                    sh '''
                    go mod tidy
                    go mod download
                    go build -o employee-api .
                    '''
                }
            }
        }

        post {
            success {
                echo 'Compilation successful!'
            }

            failure {
                echo 'Compilation failed!'
            }
        }
    }
}
