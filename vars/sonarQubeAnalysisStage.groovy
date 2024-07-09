import org.Opstree.jenkins.stages.SonarQubeAnalysisStage

def call() {
    new SonarQubeAnalysisStage().call()
}
