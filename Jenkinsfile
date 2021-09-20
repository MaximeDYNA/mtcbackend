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

          docker.withRegistry('https://registry.hub.docker.com', 'c18ad32e-8713-4d37-9160-08e52018d5fb') {
                 dockerImage.push("${env.BUILD_NUMBER}")
                 dockerImage.push("latest")


          }
        }
      }
    }
  }
}