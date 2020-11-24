pipeline {
  environment {
    registry = "gustavoapolinario/docker-test"
    registryCredential = 'dockerhub'
    dockerImage = ''
	  IMAGE = readMavenPom().getArtifactId()
        VERSION = readMavenPom().getVersion()
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
	      when {
                branch 'master'  //only run these steps on the master branch
            }
      steps{
        script {
          sh """
	  docker build -t ${IMAGE} .
	  docker tag ${IMAGE} ${IMAGE}:${VERSION}
	  """
        }
      }
    }
	
	}
}

