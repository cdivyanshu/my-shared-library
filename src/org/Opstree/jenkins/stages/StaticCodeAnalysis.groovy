package org.Opstree.jenkins.stages

class StaticCodeAnalysis {
    static void call(Map params) {
        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv("${params.SONARQUBE_ENV_NAME}") {
                    sh "${SCANNER_HOME}/bin/sonar-scanner"
                }
            }
        }
    }
}
