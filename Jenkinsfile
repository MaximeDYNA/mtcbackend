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
       		def projectImage = docker.build("imageName:1.0", "-f /home/mtcbackend/Dockerfile .")
        }
      }
    }
    stage('Deploy Image') {
      steps{
        script {
        
			 docker.withRegistry('http://51.210.48.154:5000') {
            projectImage.push("${env.BUILD_NUMBER}")
            projectImage.push("latest")

          }
        }
      }
    }
	
	}
}
