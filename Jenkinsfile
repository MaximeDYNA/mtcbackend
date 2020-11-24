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
	
		sh " sudo mkdir -p /home/mtcbackend"
		sh "sudo pwd && ls -la target "
		sh "sudo cp target/mtc-*.war /home/mtcbackend"
		sh "sudo cp Dockerfile /home/mtcbackend"
		
		}
	}
	
	stage('Code Analysis')
          {
            steps
             {
              
			  script
              {
                      sh "mvn sonar:sonar -Dsonar.host.url=http://51.210.48.154:9000"
              }
			  
            }
          }
		  
	stage('Building image') {
      steps{
        script {
          dockerImage = docker.build("managementtools", "-f /home/mtcbackend/Dockerfile .")
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
