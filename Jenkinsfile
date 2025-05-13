pipeline {
    agent any

    tools {
        jdk 'JDK 21'  // This must match the name configured in Jenkins Global Tool Configuration
    }

    environment {
        PATH = "${tool 'JDK 21'}/bin:${env.PATH}"  // Prepend Java 21 to PATH
    }

    stages {
        stage('Verify Java Setup') {
            steps {
                sh '''
                    echo "=============================="
                    echo "➡️ JAVA_HOME: $JAVA_HOME"
                    echo "➡️ PATH: $PATH"
                    echo "➡️ which java: $(which java)"
                    java -version
                    echo "=============================="
                '''
            }
        }

        stage('Backend Build & Tests') {
            steps {
                sh '''
                    echo "➡️ Running Gradle Wrapper build for backend..."
                    ./gradlew clean test --stacktrace --info
                '''
            }
        }
    }
}