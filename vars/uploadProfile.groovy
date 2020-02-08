def call(){
  pipeline {
    agent any
    stages {
      stage('Validate Profile') {
        steps {
          sh 'inspec check .'
        }
      }
      stage('Log into Automate') {
        steps {
          sh '''#!/bin/sh -e
                inspec compliance login $AUTOMATE_SERVER_NAME --insecure --user=$AUTOMATE_USER --ent=$AUTOMATE_ENTERPRISE --dctoken=$DC_TOKEN'''
        }
      }
      stage('Upload Profile') {
        steps {
          sh 'inspec compliance upload . --overwrite'
        }
      }
      stage('Log out of Automate') {
        steps {
          sh 'inspec compliance logout'
        }
      }
    }
  }
}