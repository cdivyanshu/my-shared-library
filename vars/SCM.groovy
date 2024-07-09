def call(String repoUrl = '', String branch = 'main') {
    checkout([$class: 'GitSCM', 
              branches: [[name: branch]],
              userRemoteConfigs: [[url: repoUrl]]
            ])
}
