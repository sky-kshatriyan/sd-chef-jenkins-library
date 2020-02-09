def call(){
  pipeline {
    agent any
    stages {
      stage('Stage Environments') {
        steps {
          if(isUnix()){
            sh '''
            if [ ! -d "/var/lib/jenkins/chef_automation/global_envs" ]; then
              mkdir -p /var/lib/jenkins/chef_automation/global_envs
            fi
            cp * /var/lib/jenkins/chef_automation/global_envs/
            '''
          }else{
            powershell '''
              if (!(Test-Path -Path "D:\var\lib\jenkins\chef_automation\global_envs")) {
                    mkdir -p "D:\var\lib\jenkins\chef_automation\global_envs"
                }
            '''
          }
        }
      }
      stage('Publish Environments to Production') {
        steps {
          input 'Publish Enviornments to Production Chef Server?'
          powershell ''' 
          chef exec ruby D:\var\lib\jenkins\chef_automation\update_global_env_pins.rb -k D:\var\lib\jenkins\chef_repo\.chef\knife.rb -f D:\var\lib\jenkins\chef_automation
          '''
        }
      }
    }
  }
}
