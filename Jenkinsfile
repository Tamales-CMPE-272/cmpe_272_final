pipeline {
    agent any

    environment {
        sdkman_curl_connect_timeout = '60'  // Increase from 7s to 30s
        sdkman_curl_max_time = '300'        // Increase from 10s to 300s (5 minutes)
    }

    stages {
        stage('Install JDK 21 via SDKMAN') {
            steps {
                sh '''
                    # Install SDKMAN if not already installed
                    curl -s "https://get.sdkman.io" | bash
                    source "$HOME/.sdkman/bin/sdkman-init.sh"

                    # Install JDK 21 if not already installed
                    sdk install java 21.0.2-tem || true

                    # Use JDK 21
                    sdk use java 21.0.2-tem

                    # Verify installation
                    java -version
                '''
            }
        }

        stage('Install Gradle via SDKMAN') {
            steps {
                sh '''
                    source "$HOME/.sdkman/bin/sdkman-init.sh"
                    sdk install gradle  # Adjust version as needed
                    gradle --version
                '''
            }
        }
    }
}
