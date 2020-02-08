def call(String projName){
  pipeline {
    agent any
    stages {
      stage('Pre-Cleanup') {
        steps {
          sh '''
            rm -rf ~/chef_repo/cookbooks/${proj_name}
          '''
        }
      }
      stage('Verify') {
        parallel {
          stage('Lint') {
            steps {
              sh 'chef exec foodcritic .'
            }
          }
          stage('Syntax') {
            steps {
              sh 'chef exec cookstyle .'
            }
          }
          stage('Unit') {
            steps {
              sh 'chef exec rspec .'
            }
          }
        }
      }
      stage('Smoke') {
        steps {
          sh '# kitchen test'
        }
      }
      stage('Stage files') {
        when {
          // Only execute when on the master branch
          expression { env.BRANCH_NAME == 'master' }
        }
        steps {
          sh '''
            mkdir -p ~/chef_repo/cookbooks/${proj_name}
            cp -r * ~/chef_repo/cookbooks/${proj_name}
          '''
        }
      }
      stage('Generate Lock File') {
        when {
          // Only execute when on the master branch
          expression { env.BRANCH_NAME == 'master' }
        }
        steps {
          sh '''
            cd ~/chef_repo/cookbooks/${proj_name}
            chef install
          '''
        }
      }
      stage('Dev Deployment') {
        when {
          // Only execute when on the master branch
          expression { env.BRANCH_NAME == 'master' }
        }
        steps {
          sh '''
          cd ~/chef_repo/cookbooks/${proj_name}
          chef push dev
          '''
        }
      }
      stage('Approve Staging Deployment?') {
        when {
          // Only execute when on the master branch
          expression { env.BRANCH_NAME == 'master' }
        }
        steps {
          input "Deploy ${proj_name} to Staging?"
        }
      }
      stage('Staging Deployment') {
        when {
          // Only execute when on the master branch
          expression { env.BRANCH_NAME == 'master' }
        }
        steps {
          sh '''
          cd ~/chef_repo/cookbooks/${proj_name}
          chef push stg
          '''
        }
      }
      stage('Approve Prod Deployment?') {
        when {
          // Only execute when on the master branch
          expression { env.BRANCH_NAME == 'master' }
        }
        steps {
          input "Deploy ${proj_name} to Prod?"
        }
      }
      stage('Prod Deployment') {
        when {
          // Only execute when on the master branch
          expression { env.BRANCH_NAME == 'master' }
        }
        steps {
          sh '''
          cd ~/chef_repo/cookbooks/${proj_name}
          chef push prod
          '''
        }
      }
      stage('Post-Cleanup') {
        when {
          // Only execute when on the master branch
          expression { env.BRANCH_NAME == 'master' }
        }
        steps {
          sh '''
            rm -rf ~/chef_repo/cookbooks/${proj_name}
          '''
        }
      }
    }
    environment {
      proj_name = "${projName}"
    }
  }
}