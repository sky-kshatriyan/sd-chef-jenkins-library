pipeline {
  agent any
  stages {
    stage('Pre-req Tests') {
      parallel {
        stage('Check ChefDK') {
          steps {
              powershell ''' 
                $ChefDK = (Test-Path -Path 'C:\\tools\\opscode\\chefdk\\bin\\chef')
                if ($ChefDK) {
                    Write-Host "Chef is Installed"
                }else {
                    Write-Host "ChefDK is missing! Please visit https://downloads.chef.io."
                    Exit 1
                }
              '''
          }
        }
        stage('Check the chef_repo') {
          steps {
            powershell label: 'CHEF REPO CHECK', script: '''                
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
                                New-Item -ItemType File -Path "D:\\var\\lib\\jenkins\\chef_repo\\.chef" -Name \'knife.rb\'
                            }
                            if (!(Test-Path -Path "D:\\var\\lib\\jenkins\\chef_repo\\.chef\\client.pem")) {
                                New-Item -ItemType File -Path "D:\\var\\lib\\jenkins\\chef_repo\\.chef" -Name \'client.pem\'
                            }'''
          }
        }
      }
    }
    stage('Verify Ruby files') {
      steps {
        powershell '''
         chef exec rubocop utilities/.
          '''
      }
    }
    stage('Stage Utilities') {
      steps {
          powershell '''
            if (!(Test-Path -Path "D:\\var\\lib\\jenkins\\chef_automation")) {
                mkdir -p "D:\\var\\lib\\jenkins\\chef_automation"
            }
            cp utilities\\* -Destination "D:\\var\\lib\\jenkins\\chef_automation"
          '''        
        // if(isUnix()){
        //   sh '''
        //   if [ ! -d "/var/lib/jenkins/chef_automation" ]; then
        //     mkdir -p /var/lib/jenkins/chef_automation
        //   fi
        //   cp utilities/* /var/lib/jenkins/chef_automation/
        //   '''
        // }else{
        //   powershell '''
        //     if (!(Test-Path -Path "D:\\var\\lib\\jenkins\\chef_automation")) {
        //         mkdir -p "D:\\var\\lib\\jenkins\\chef_automation'
        //     }          
        //   '''
        // }
      }
    }
  }
}