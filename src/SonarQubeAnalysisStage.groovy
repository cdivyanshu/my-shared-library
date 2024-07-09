package org.Opstree.jenkins.stages

def call(Map params) {
    sh '''
        withSonarQubeEnv("${params.SONARQUBE_ENV_NAME}") {
            sh "${SCANNER_HOME}/bin/sonar-scanner"
        }
    '''
}
