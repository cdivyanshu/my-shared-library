package org.Opstree.Jenkins.stages

def call() {
    sh '''
        git clone "https://github.com/Naresh-boyini/employee-api.git"
    '''
}
