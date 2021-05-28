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
        sh "mvn clean install -DskipTests=true"
	}
	stage('Test')
    {
            steps
            {
              sh "${mvnCmd} test -Dspring.profiles.active=test"
              //step([$class: 'JUnitResultArchiver', testResults: '**/target/surefire-reports/TEST-*.xml'])
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
          dockerImage = docker.build managementtools
        }
      }
    }
    stage('Deploy Image') {
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