pipeline {
    agent any

    tools {
        maven 'maven'
        // jdk 'JDK 17' // Commented out as no JDK installations are configured in Jenkins
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Install Dependencies') {
            steps {
                sh 'mvn clean install -DskipTests'
            }
        }

        stage('Run QA Automation') {
            steps {
                // Ensure Playwright browsers are installed
                sh 'mvn exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install chromium"'
                // Run parallel test suite
                sh 'mvn test'
            }
            post {
                always {
                    // Generate and publish Allure Report
                    allure includeProperties: false, jdk: '', results: [[path: 'target/allure-results']]
                }
            }
        }
    }
}
