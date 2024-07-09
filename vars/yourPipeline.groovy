// vars/yourPipeline.groovy
def call(Map params) {
    org.yourorganization.jenkins.stages.CompileCode compileCode = new org.yourorganization.jenkins.stages.CompileCode()
    org.yourorganization.jenkins.stages.StaticCodeAnalysis staticCodeAnalysis = new org.yourorganization.jenkins.stages.StaticCodeAnalysis()
    org.yourorganization.jenkins.stages.BugsAnalysis bugsAnalysis = new org.yourorganization.jenkins.stages.BugsAnalysis()
    org.yourorganization.jenkins.stages.DependencyScanning dependencyScanning = new org.yourorganization.jenkins.stages.DependencyScanning()
    org.yourorganization.jenkins.stages.Dast dast = new org.yourorganization.jenkins.stages.Dast()

    pipeline {
        agent any

        parameters {
            string(name: 'GIT_BRANCH', defaultValue: 'main', description: 'Git branch to checkout')
            string(name: 'GIT_URL', defaultValue: 'https://github.com/Naresh-boyini/employee-api.git', description: 'Git repository URL')
            string(name: 'SCANNER_TOOL_NAME', defaultValue: 'sonar', description: 'Name of the SonarQube Scanner tool as configured in Jenkins')
            string(name: 'SONARQUBE_ENV_NAME', defaultValue: 'sonar-kumar', description: 'SonarQube environment name')
            string(name: 'ZAP_PORT', defaultValue: '8080', description: 'Port for ZAP to use')
            string(name: 'ZAP_URL', defaultValue: 'http://34.173.102.226/api/v1/employee/health', description: 'URL for ZAP to scan')
        }

        stages {
            stage('Code Compilation') {
                steps {
                    script {
                        compileCode(
                            GIT_BRANCH: params.GIT_BRANCH,
                            GIT_URL: params.GIT_URL
                        )
                    }
                }
            }

            stage('Static Code Analysis') {
                steps {
                    script {
                        staticCodeAnalysis(
                            SCANNER_TOOL_NAME: params.SCANNER_TOOL_NAME,
                            SONARQUBE_ENV_NAME: params.SONARQUBE_ENV_NAME
                        )
                    }
                }
            }

            stage('Bugs Analysis') {
                steps {
                    script {
                        bugsAnalysis()
                    }
                }
            }

            stage('Dependency Scanning') {
                steps {
                    script {
                        dependencyScanning()
                    }
                }
            }

            stage('Dynamic Application Security Testing (DAST)') {
                steps {
                    script {
                        dast(
                            ZAP_PORT: params.ZAP_PORT,
                            ZAP_URL: params.ZAP_URL
                        )
                    }
                }
            }
        }

        post {
            success {
                echo 'Build successful!'
            }

            failure {
                echo 'Build failed!'
            }
        }
    }
}
