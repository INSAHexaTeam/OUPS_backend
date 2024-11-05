pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                // Compiler le projet
                sh 'mvn clean package'
            }
        }
        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }
    }
    post {
        success {
            echo 'La compilation et les tests ont été réussis !'
        }
        failure {
            echo 'Il y a eu un échec lors de la compilation ou des tests.'
        }
    }
}