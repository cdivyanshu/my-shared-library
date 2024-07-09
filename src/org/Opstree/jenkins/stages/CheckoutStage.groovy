package org.Opstree.jenkins.stages

def call(Map params) {
    sh '''
        git branch: "${params.GIT_BRANCH}", url: "${params.GIT_URL}"
    '''
}
