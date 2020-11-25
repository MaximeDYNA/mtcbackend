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
        sh "mvn clean package -DskipTests=true"
		
		}
	}
	
	stage('changedirectory'){
	steps{
	
		sh " pwd && ls -la target "
		sh " cp target/mtc-*.war /home/mtcbackend"
		sh " cp Dockerfile /home/mtcbackend"
		
		}
	}
		  
	stage('Building image') {
      steps{
	 echo 'Starting to build docker image'
        script {
		def dockerfile = 'Dockerfile'
		def dockerImage = docker.build("management-image", "-f ${dockerfile} /home/mtcbackend")
        }
      }
    }
    stage('Deploy Image') {
      steps{
        script {
        
			 docker.withRegistry('http://51.210.48.154:5000') {
            dockerImage.push("${env.BUILD_NUMBER}")
            dockerImage.push("latest")

          }
        }
      }
    }
	
	}
}
