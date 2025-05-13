pipeline {
    agent any

    tools {
        jdk 'JDK 21 SDKMAN'
    }

    environment {
        PATH = "${tool 'JDK 21'}/bin:${env.PATH}"
    }

    stages {
        stage('Verify Java Setup') {
            steps {
                sh '''
                    echo "JAVA_HOME: $JAVA_HOME"
                    echo "PATH: $PATH"
                    echo "which java: $(which java)"
                    echo "java -version (via JAVA_HOME):"
                    $JAVA_HOME/bin/java -version
                '''
            }
        }

        stage('Backend Build & Tests') {
            steps {
                sh '''
                    cd backend/tamalesHr
                    echo "Running Gradle Wrapper build..."
                    ./gradlew clean test --stacktrace --info
                '''
            }
        }
    }
}
