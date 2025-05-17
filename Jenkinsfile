pipeline {
    agent any

    environment {
        sdkman_curl_connect_timeout = '60'  
        sdkman_curl_max_time = '300'
    }

    tools {
        gradle 9.0
    }

    stages {
        stage('Backend Build & Tests') {
            steps {
                sh '''
                    # Install SDKMAN if not already installed
                    curl -s "https://get.sdkman.io" | bash
                    source "$HOME/.sdkman/bin/sdkman-init.sh"

                    sdk list java

                    # Use JDK 21
                    sdk use java 21.0.2-tem

                    # Verify installation
                    java -version

                    gradle --version

                    cd backend/tamalesHr
                    echo "Running Gradle Tests..."
                    gradle test --stacktrace --info
                '''
            }
        }
    }
}
