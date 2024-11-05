pipeline {
    agent any // Usar cualquier agente disponible
    stages {
        stage('Verify Calculation') {
            steps {
                script {
                    // Verificar que 1 + 1 es igual a 2
                    def result = 1 + 1
                    if (result == 2) {
                        echo 'La verificación es exitosa: 1 + 1 es igual a 2.'
                    } else {
                        error 'La verificación falló: 1 + 1 no es igual a 2.'
                    }
                }
            }
        }
    }
}
