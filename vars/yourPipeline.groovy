import org.Opstree.jenkins.stages.CompileCode
import org.Opstree.jenkins.stages.StaticCodeAnalysis
import org.Opstree.jenkins.stages.BugsAnalysis
import org.Opstree.jenkins.stages.DependencyScanning
import org.Opstree.jenkins.stages.Dast

def call(Map params) {
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
                        CompileCode.call(
                            GIT_BRANCH: params.GIT_BRANCH,
                            GIT_URL: params.GIT_URL
                        )
                    }
                }
            }

            stage('Static Code Analysis') {
                steps {
                    script {
                        StaticCodeAnalysis.call(
                            SCANNER_TOOL_NAME: params.SCANNER_TOOL_NAME,
                            SONARQUBE_ENV_NAME: params.SONARQUBE_ENV_NAME
                        )
                    }
                }
            }

            stage('Bugs Analysis') {
                steps {
                    script {
                        BugsAnalysis.call()
                    }
                }
            }

            stage('Dependency Scanning') {
                steps {
                    script {
                        DependencyScanning.call()
                    }
                }
            }

            stage('Dynamic Application Security Testing (DAST)') {
                steps {
                    script {
                        Dast.call(
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
