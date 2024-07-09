package org.Opstree.jenkins.stages

class SonarQubeAnalysisStage {
    static void stage(Map params) {
        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv(params.SONARQUBE_ENV_NAME) {
                    sh "${params.SCANNER_HOME}/bin/sonar-scanner"
                }
            }
        }
    }
}
