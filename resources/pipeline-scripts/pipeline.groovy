@Library('my-shared-library') _

import org.jenkinsci.myPipeline

myPipeline(
    repoUrl: 'https://github.com/Naresh-boyini/employee-api.git',
    branch: 'main'
)
