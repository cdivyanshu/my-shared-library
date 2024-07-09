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
            string(name: 'ZAP_PORT', defaultValue: '8080', description:
