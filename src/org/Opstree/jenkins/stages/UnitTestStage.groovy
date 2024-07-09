package org.Opstree.jenkins.stages

class UnitTestStage {
    static void stage() {
        stage('Unit Test') {
            steps {
                sh 'go test ./... -v | tee unit-test-results.xml'
            }
        }
    }
}
