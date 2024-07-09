package org.Optree.jenkins.stages

class DependencyScanning {
    static void call() {
        stage('Dependency Scan') {
            steps {
                dependencyCheck additionalArguments: '--scan . --format ALL', odcInstallation: 'OWASP Dependency-Check Vulnerabilities'
            }
        }
    }
}
