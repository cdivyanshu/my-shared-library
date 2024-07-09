package org.Opstree.jenkins.stages

def call(Map params) {
    sh '''
        export GO111MODULE=on
        sed -i 's/go 1.20/go 1.18/g' go.mod
    '''
}
