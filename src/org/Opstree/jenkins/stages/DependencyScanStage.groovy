package org.Opstree.jenkins.stages

class DependencyScanStage {
    static void stage(Map params) {
        stage('Dependency Scan') {
            steps {
                // Run OWASP Dependency-Check
                dependencyCheck additionalArguments: '--scan . --format ALL', odcInstallation: params.DEPENDENCY_TOOL_NAME
            }
        }
    }
}

