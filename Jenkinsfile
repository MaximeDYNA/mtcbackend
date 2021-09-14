pipeline {
  environment {
    registry = "gustavoapolinario/docker-test"
    registryCredential = 'dockerhub'
    dockerImage = ''
  }
  agent any
	stage('clone'){
		git branch: 'develop', credentialsId: 'Mtc_Git', url: 'git@github.com:CATIS-DEVELOPER/mtc.git'
		script {
                  def pom = readMavenPom file: 'pom.xml'
                  version = pom.version
              }
        sh "mvn package -Dmaven.test.skip=true"
	}
	stage('Build') {
      steps{
        script {
          dockerImage = docker.build managementtools
        }
      }
    }
    stage('Run') {
      steps{
        script {
          
          docker.withRegistry( 'http://51.210.48.154:5000' ) {
            /*dockerImage.push("$BUILD_NUMBER")*/
             dockerImage.push('latest')

          }
        }
      }
    }
}