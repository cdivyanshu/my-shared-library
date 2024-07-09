import org.Opstree.jenkins.stages.DependencyScanStage

def call() {
    new DependencyScanStage().call()
}
