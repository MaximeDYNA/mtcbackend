pipeline {
  environment {
    registry = "gustavoapolinario/docker-test"
    registryCredential = 'dockerhub'
    dockerImage = ''
  }
  agent any
  stages {
   
	stage('clone'){
	steps{
		git branch: 'master', credentialsId: 'Mtc_Git', url: 'git@github.com:CATIS-DEVELOPER/mtc.git'
        sh "mvn clean install -DskipTests=true"
		}
	}
		  
	stage('Building image') {
	  
      steps{
        script {
          sh """
	  docker build -t mtc .
	  docker tag mtc mtc:1.0
	  """
        }
      }
    }
	
	}
}

