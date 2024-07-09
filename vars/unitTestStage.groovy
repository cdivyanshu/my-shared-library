import org.Opstree.jenkins.stages.UnitTestStage

def call() {
    new UnitTestStage().call()
}
