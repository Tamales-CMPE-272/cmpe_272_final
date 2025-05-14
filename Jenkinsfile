pipeline {
    agent any

    environment {
        sdkman_curl_connect_timeout = '60'  // Increase from 7s to 30s
        sdkman_curl_max_time = '300'        // Increase from 10s to 300s (5 minutes)
    }

    tools {
        gradle 9.0
    }

    stages {
        stage('Install JDK 21 via SDKMAN') {
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

                    ./gradlew --version
                '''
            }
        }
    }
}
