pipeline {
    agent any  

    tools {
        maven 'maven-3.9.11' 
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'master', 
                url: 'https://github.com/VigRamKrish/Selenium_Automation_Framework_OrangeHRM.git'
            }
        }

        stage('Build & Test') {
            steps {
                bat 'mvn clean test'
            }
        }

        stage('Publish Reports') {
            steps {
                publishHTML(target: [
                    reportDir: 'src/test/resources/extentreport',
                    reportFiles: 'ExtentReportIndex.html',
                    reportName: 'Extent Report',
                    keepAll: true,
                    alwaysLinkToLastBuild: true,
                    allowMissing: true
                ])
            }
        }
    }

    post {

        always {
            archiveArtifacts artifacts: '**/src/test/resources/extentreport/*.html', fingerprint: true
            junit 'target/surefire-reports/*.xml'
        }

        success {
            emailext (
                to: 'rvigneshram150@gmail.com',
                subject: "Build Success: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                mimeType: 'text/html',
                attachLog: true,
                body: """
                <html>
                <body>
                <p>Hello Team,</p>
                <p>The latest Jenkins build has completed successfully.</p>

                <p><b>Project Name:</b> ${env.JOB_NAME}</p>
                <p><b>Build Number:</b> #${env.BUILD_NUMBER}</p>
                <p><b>Build Status:</b> <span style="color: green;"><b>SUCCESS</b></span></p>
                <p><b>Build URL:</b> <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>
                <p><b>Extent Report:</b> 
                <a href="${env.BUILD_URL}HTML_20Extent_20Report/">Click here</a></p>

                <p>Best regards,<br>
                <b>Automation Team</b></p>
                </body>
                </html>
                """
            )
        }

        failure {
            emailext (
                to: 'rvigneshram150@gmail.com',
                subject: "Build Failed: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                mimeType: 'text/html',
                attachLog: true,
                body: """
                <html>
                <body>
                <p>Hello Team,</p>
                <p>The latest Jenkins build has 
                <b style="color: red;">FAILED</b>.</p>

                <p><b>Project Name:</b> ${env.JOB_NAME}</p>
                <p><b>Build Number:</b> #${env.BUILD_NUMBER}</p>
                <p><b>Build Status:</b> 
                <span style="color: red;"><b>FAILED ❌</b></span></p>

                <p><b>Build URL:</b> 
                <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>

                <p><b>Extent Report (if available):</b> 
                <a href="${env.BUILD_URL}HTML_20Extent_20Report/">Click here</a></p>

                <p>Please check the logs and take necessary actions.</p>

                <p>Best regards,<br>
                <b>Automation Team</b></p>
                </body>
                </html>
                """
            )
        }
    }
}
