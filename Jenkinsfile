pipeline {
    agent any

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
