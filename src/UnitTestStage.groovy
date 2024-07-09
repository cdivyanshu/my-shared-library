package org.Opstree.jenkins.stages

def call(Map params) {
    sh '''
        go test ./... -v | tee unit-test-results.xml
    '''
}
