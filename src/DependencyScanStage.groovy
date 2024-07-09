package org.Opstree.jenkins.stages

def call(Map params) {
    sh '''
        dependencyCheck additionalArguments: '--scan . --format ALL', odcInstallation: "${params.DEPENDENCY_TOOL_NAME}"
    '''
}


