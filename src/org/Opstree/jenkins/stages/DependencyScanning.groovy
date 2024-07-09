package org.Opstree.jenkins.stages

class DependencyScanning {
    static void call() {
        stage('Dependency Scan') {
            steps {
                dependencyCheck additionalArguments: '--scan . --format ALL', odcInstallation: 'OWASP Dependency-Check Vulnerabilities'
            }
        }
    }
}
