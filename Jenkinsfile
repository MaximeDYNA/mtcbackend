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
          dockerImage = docker.build("chefban/distros-backend")
        }
      }
    }
    stage('Run') {
      steps{
        script {

          docker.withRegistry('https://registry.hub.docker.com', 'git') {
                 app.push("${env.BUILD_NUMBER}")
                 app.push("latest")
          

          }
        }
      }
    }
  }
}