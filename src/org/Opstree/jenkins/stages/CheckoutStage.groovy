package org.Opstree.jenkins.stages

def call(Map params) {
    sh '''
        git clone "https://github.com/Naresh-boyini/employee-api.git"
    '''
}
