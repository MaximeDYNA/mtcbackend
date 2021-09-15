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
            git branch: 'develop', credentialsId: 'bfbbd2d8-0c10-4887-ba75-cae23c18f8ec', url: 'https://github.com/CATIS-DEVELOPER/mtc.git'
            script {
                      def pom = readMavenPom file: 'pom.xml'
                      version = pom.version
                  }
            sh "mvn package -Dmaven.test.skip=true"
        }
	}
	stage('Build') {
      steps{
        script {
          dockerImage = docker.build("Ditros:${env.BUILD_ID}")
        }
      }
    }
    stage('Run') {
      steps{
        script {
          
          docker.withRegistry( 'http://135.125.244.28:5000' ) {
            /*dockerImage.push("$BUILD_NUMBER")*/
             dockerImage.push('latest')

          }
        }
      }
    }
  }
}