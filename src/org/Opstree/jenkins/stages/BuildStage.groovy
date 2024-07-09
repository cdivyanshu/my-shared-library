package org.Opstree.jenkins.stages

class BuildStage {
    static void stage() {
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
}
