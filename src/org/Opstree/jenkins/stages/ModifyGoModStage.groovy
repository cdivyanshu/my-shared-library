package org.Opstree.jenkins.stages

class ModifyGoModStage {
    static void stage() {
        stage('Modify go.mod') {
            steps {
                sh '''
                export GO111MODULE=on
                sed -i 's/go 1.20/go 1.18/g' go.mod
                '''
            }
        }
    }
}
