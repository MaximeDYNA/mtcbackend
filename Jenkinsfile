pipeline {
    agent any

    environment {
        // Variables d'environnement
        GIT_REPO = 'https://github.com/MaximeDYNA/mtcbackend.git'  // L'URL HTTPS de ton dépôt
        MAVEN_HOME = tool(name: 'Maven 3.8.1', type: 'maven')  // Chemin de Maven
        JAVA_HOME = tool(name: 'JDK 11', type: 'jdk')  // Version de Java
    }

    stages {
        stage('Cloner le dépôt') {
            steps {
                // Cloner le dépôt Git via HTTPS avec token d'accès personnel
                git branch: 'backend', url: "${GIT_REPO}"
            }
        }

        stage('Compiler l\'application') {
            steps {
                // Compiler le projet avec Maven
                sh "${MAVEN_HOME}/bin/mvn clean compile"
            }
        }

        stage('Effectuer les tests') {
            steps {
                // Lancer les tests unitaires
                sh "${MAVEN_HOME}/bin/mvn test"
            }
        }

        stage('Construire le package') {
            steps {
                // Construire l'artefact
                sh "${MAVEN_HOME}/bin/mvn package"
            }
            post {
                success {
                    // Sauvegarder le JAR/WAR
                    archiveArtifacts artifacts: 'target/*.jar', allowEmptyArchive: false
                }
            }
        }

        stage('Déployer l\'application') {
            steps {
                // Déploiement avec Ansible
                sh "ansible-playbook -i inventory/development deploy-backend.yml"
            }
        }
    }

    post {
        always {
            // Nettoyage après la construction
            cleanWs()
        }
        success {
            // Notification de succès
            echo 'Build réussi avec succès !'
        }
        failure {
            // Notification d'échec
            echo 'Build échoué !'
        }
    }
}
//, credentialsId: 'your-https-credentials-id'
