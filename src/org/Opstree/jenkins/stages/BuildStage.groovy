package org.Opstree.jenkins.stages

def call(Map params) {
    sh '''
        go mod tidy
        go mod download
        go build -o employee-api .
    '''
}
