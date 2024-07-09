@Library('my-shared-library') _

pipeline {
    agent any

    parameters {
        string(
            name: 'STAGES_TO_RUN',
            defaultValue: 'Checkout, Modify go.mod, Build, Unit Test, SonarQube Analysis, Dependency Scan, Archive Report',
            description: 'Comma-separated list of stages to run'
        )
        string(
            name: 'GO_TOOL_NAME',
            defaultValue: 'Go',
            description: 'Name of the Go tool as configured in Jenkins'
        )
        string(
            name: 'SCANNER_TOOL_NAME',
            defaultValue: 'sonar',
            description: 'Name of the SonarQube Scanner tool as configured in Jenkins'
        )
        string(
            name: 'DEPENDENCY_TOOL_NAME',
            defaultValue: 'Dependency-Check',
            description: 'Name of the dependency scanning tool as configured in Jenkins'
        )
        string(
            name: 'GIT_BRANCH',
            defaultValue: 'main',
            description: 'Git branch to checkout'
        )
        string(
            name: 'GIT_URL',
            defaultValue: 'https://github.com/Naresh-boyini/employee-api.git',
            description: 'Git repository URL'
        )
        string(
            name: 'SONARQUBE_ENV_NAME',
            defaultValue: 'sonar-kumar',
            description: 'SonarQube environment name'
        )
    }

    environment {
        SCANNER_HOME = tool(params.SCANNER_TOOL_NAME) // Define SCANNER_HOME for SonarQube
        DEPENDENCY_CHECK_HOME = tool(params.DEPENDENCY_TOOL_NAME) // Define DEPENDENCY_CHECK_HOME for Dependency-Check
        GO_VERSION = '1.18' // Define GO_VERSION for Go
    }

    stages {
        stage('Checkout') {
            when {
                expression {
                    def stagesToRun = params.STAGES_TO_RUN.split(',').collect { it.trim() }
                    return stagesToRun.contains('Checkout')
                }
            }
            steps {
                git branch: params.GIT_BRANCH, url: params.GIT_URL
            }
        }

        stage('Modify go.mod') {
            when {
                expression {
                    def stagesToRun = params.STAGES_TO_RUN.split(',').collect { it.trim() }
                    return stagesToRun.contains('Modify go.mod')
                }
            }
            steps {
                sh '''
                export GO111MODULE=on
                sed -i 's/go 1.20/go 1.18/g' go.mod
                '''
            }
        }

        stage('Build') {
            when {
                expression {
                    def stagesToRun = params.STAGES_TO_RUN.split(',').collect { it.trim() }
                    return stagesToRun.contains('Build')
                }
            }
            steps {
                sh '''
                go mod tidy
                go mod download
                go build -o employee-api .
                '''
            }
        }

        stage('Unit Test') {
            when {
                expression {
                    def stagesToRun = params.STAGES_TO_RUN.split(',').collect { it.trim() }
                    return stagesToRun.contains('Unit Test')
                }
            }
            steps {
                sh 'go test ./... -v | tee unit-test-results.xml'
            }
        }

        stage('SonarQube Analysis') {
            when {
                expression {
                    def stagesToRun = params.STAGES_TO_RUN.split(',').collect { it.trim() }
                    return stagesToRun.contains('SonarQube Analysis')
                }
            }
            steps {
                withSonarQubeEnv(params.SONARQUBE_ENV_NAME) {
                    sh "${SCANNER_HOME}/bin/sonar-scanner"
                }
            }
        }

        stage('Dependency Scan') {
            when {
                expression {
                    def stagesToRun = params.STAGES_TO_RUN.split(',').collect { it.trim() }
                    return stagesToRun.contains('Dependency Scan')
                }
            }
            steps {
                // Run OWASP Dependency-Check
                dependencyCheck additionalArguments: '--scan . --format ALL', odcInstallation: params.DEPENDENCY_TOOL_NAME
            }
        }

        stage('Archive Report') {
            when {
                expression {
                    def stagesToRun = params.STAGES_TO_RUN.split(',').collect { it.trim() }
                    return stagesToRun.contains('Archive Report')
                }
            }
            steps {
                // Archive the unit test results
                archiveArtifacts artifacts: 'unit-test-results.xml', allowEmptyArchive: true

                echo "Publishing Reports"
                // Publish OWASP Dependency-Check report in HTML format
                publishHTML([
                    allowMissing: false,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: '.',
                    reportFiles: 'dependency-check-report.html',
                    reportName: 'Dependency Check Report',
                    reportTitles: ''
                ])
            }
        }
    }

    post {
        success {
            echo 'Build successful!'
            // Additional actions on success
        }

        failure {
            echo 'Build failed!'
            // Additional actions on failure
        }
    }
}
