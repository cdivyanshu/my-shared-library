@Library('shared-library') _

node {

    // Define the Go tool installation
    def goTool = tool name: 'Go', type: 'go'
    // 'Go' here is the name of the Go installation configured in Jenkins

    // Define DEPENDENCY_CHECK_HOME for Dependency-Check
    def dependencyCheckHome = tool name: 'Dependency-Check'

    // Set environment variables
    env.DEPENDENCY_CHECK_HOME = dependencyCheckHome

    stage('Checkout') {
        generic.checkout('https://github.com/mygurkulam-p9/Employee-API.git', 'github-token1', 'main')
    }
     stage('Code Complation') {
        golangci.executeCodeComplation()
    }
    
   stage('Run Unit Tests') {
        golangci.runUnitTests()
    }

    stage('Bug Analysis') {
      golangci.bug()
    }
    
    stage('Dependency Check') {
        golangci.dependencyCheck()
    }
    stage('DAST') {
        golangci.dast()
    }
}
