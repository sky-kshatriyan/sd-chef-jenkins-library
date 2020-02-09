pipeline {
  agent any
  stages {
    stage('Pre-req Tests') {
      parallel {
        stage('Check ChefDK') {
          steps {
            if(isUnix()){
              sh '''
              # Make sure ChefDK is installed
              if [ ! $(which chef) ]; then
                echo "ChefDK is missing! Please visit https://downloads.chef.io."
                exit 1
              fi
              '''
            }else{
              powershell ''' 
                $ChefDK = (which javaa)
                if ($ChefDK) {
                    Write-Host "Chef is Installed"
                }else {
                    Write-Host "ChefDK is missing! Please visit https://downloads.chef.io."
                    Exit 1
                }
              '''
            }
          }
        }
        stage('Check the chef_repo') {
          steps {
            if(isUnix()){
              sh '''
              if [ ! -d "/var/lib/jenkins/chef_repo/.chef" ]; then
                mkdir -p /var/lib/jenkins/chef_repo/.chef
              fi
              if [ ! -d "/var/lib/jenkins/chef_repo/cookbooks" ]; then
                mkdir -p /var/lib/jenkins/chef_repo/cookbooks
              fi
              if [ ! -d "/var/lib/jenkins/chef_repo/environments" ]; then
                mkdir -p /var/lib/jenkins/chef_repo/environments
              fi
              if [ ! -f "/var/lib/jenkins/chef_repo/.chef/knife.rb" ]; then
                echo "WARNING"
                echo "We are creating empty files so the setup can proceed."
                echo "Replace the contents of /var/lib/jenkins/chef_repo/.chef/knife.rb with your information"
                touch /var/lib/jenkins/chef_repo/.chef/knife.rb
              fi
              if [ ! -f "/var/lib/jenkins/chef_repo/.chef/client.pem" ]; then
                touch /var/lib/jenkins/chef_repo/.chef/client.pem
              fi
              '''              
            }else {
              powershell '''
                if (!(Test-Path -Path "D:\\var\\lib\\jenkins\\chef_repo\\.chef")) {
                    mkdir -p "D:\\var\\lib\\jenkins\\chef_repo\\.chef"
                }
                if (!(Test-Path -Path "D:\\var\\lib\\jenkins\\chef_repo\\cookbooks")) {
                    mkdir -p "D:\\var\\lib\\jenkins\\chef_repo\\cookbooks"
                }
                if (!(Test-Path -Path "D:\\var\\lib\\jenkins\\chef_repo\\environments")) {
                    mkdir -p "D:\\var\\lib\\jenkins\\chef_repo\\environments"
                }
                if (!(Test-Path -Path "D:\\var\\lib\\jenkins\\chef_repo\\.chef\\knife.rb")) {
                    Write-Host "WARNING"
                    Write-Host "We are creating empty files so the setup can proceed."
                    Write-Host "Replace the contents of /var/lib/jenkins/chef_repo/.chef/knife.rb with your information"    
                    New-Item -ItemType File -Path "D:\\var\\lib\\jenkins\\chef_repo\\.chef" -Name 'knife.rb'
                }
                if (!(Test-Path -Path "D:\\var\\lib\\jenkins\\chef_repo\\.chef\\client.pem")) {
                    New-Item -ItemType File -Path "D:\\var\\lib\\jenkins\\chef_repo\\.chef" -Name 'client.pem'
                }
              '''                            
            }
          }
        }
      }
    }
    stage('Verify Ruby files') {
      steps {
        if(isUnix()){
          sh 'chef exec rubocop utilities/.'
        }else{
          powershell '''
            chef exec rubocop utilities/.
          '''
        }
      }
    }
    stage('Stage Utilities') {
      steps {
        if(){
          sh '''
          if [ ! -d "/var/lib/jenkins/chef_automation" ]; then
            mkdir -p /var/lib/jenkins/chef_automation
          fi
          cp utilities/* /var/lib/jenkins/chef_automation/
          '''
        }else{
          powershell '''
            if (!(Test-Path -Path "D:\\var\\lib\\jenkins\\chef_automation")) {
                mkdir -p "D:\\var\\lib\\jenkins\\chef_automation'
            }          
          '''
        }
      }
    }
  }
}