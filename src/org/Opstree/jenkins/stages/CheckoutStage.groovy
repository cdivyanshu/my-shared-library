package org.Opstree.jenkins.stages

class CheckoutStage {
    static void stage(Map params) {
        stage('Checkout') {
            steps {
                git branch: params.GIT_BRANCH, url: params.GIT_URL
            }
        }
    }
}
