package org.jenkinsci

def call(Map config = [:]) {
    pipeline {
        agent any
        
        tools {
            go 'Go' // Use the name exactly as configured in Jenkins' Global Tool Configuration
        }
        
        stages {
            stage('Checkout') {
                steps {
                    // Checkout code from GitHub specifying the branch
                    git branch: config.branch ?: 'main', url: config.repoUrl
                }
            }
            
            stage('Modify go.mod') {
                steps {
                    // Ensure Go modules are enabled
                    sh 'export GO111MODULE=on'
                    
                    // Change go.mod to use Go 1.18
                    sh 'sed -i "s/go 1.20/go 1.18/g" go.mod'
                }
            }
            
            stage('Build') {
                steps {
                    // Clean and download dependencies
                    sh 'go mod tidy'
                    sh 'go mod download'
                    
                    // Compile the Go code
                    sh 'go build -o employee-api .'
                }
            }
            
            stage('Dependency Scan') {
                steps {
                    // Run OWASP Dependency-Check
                    dependencyCheckPublisher pattern: '**',
                        includesExcludes: [[includePattern: '**']],
                        failBuildOnCVSS: '10'
                }
            }
        }
        
        // Add post-build actions or other configurations as needed
    }
}
