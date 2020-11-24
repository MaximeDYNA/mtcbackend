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
	  
	  stage('Build Image') {
            when {
                branch 'master'  //only run these steps on the master branch
            }
            steps {
                /*
                 * Multiline strings can be used for larger scripts. It is also possible to put scripts in your shared library
                 * and load them with 'libaryResource'
                 */
                sh """
          dockerImage = docker build -t ${IMAGE} .
      
            }
        }

    stage('Deploy Image') {
      steps{
        script {
          
          docker.withRegistry( 'http://51.210.48.154:5000' ) {
            dockerImage.push("$BUILD_NUMBER")
             dockerImage.push('latest')

          }
        }
      }
    }
	 
	}
}
