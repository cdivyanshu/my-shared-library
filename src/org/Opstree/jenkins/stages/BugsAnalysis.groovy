package org.Opstree.jenkins.stages

class BugsAnalysis {
    static void call() {
        stage('Unit Test') {
            steps {
                sh 'go test ./... -v | tee unit-test-results.xml'
            }
        }
    }
}
