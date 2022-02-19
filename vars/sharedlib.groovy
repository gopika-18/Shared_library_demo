def call(String repoUrl) {
pipeline {
    agent any
    tools {
            maven 'Maven' 
          }
    stages {
        stage('Clone sources') {
            steps {
                git branch: 'main',
                url: "${repoUrl}"
            }
        }
        stage('Build') {
            steps {
                sh 'mvn -V -U -e clean install -Dsurefire.useFile=false -Dmaven.test.skip=true'
            } 
        }
        stage('Test') {
            steps {
                sh 'mvn test -U -Dsurefire.useFile=false -Dsurefire.useSystemClassLoader=false'
            }
        }
        stage('Result') {
            steps {
                junit allowEmptyResults: true, checksName: '**/src/main/java/**', skipMarkingBuildUnstable: true, testResults: '**/target/surefire-reports/TEST-*.xml'
            }
        }
        stage('SonarQube analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh "mvn -V -U -e sonar:sonar"
                }
            }
        }
    }
    post {
        always {
            archiveArtifacts artifacts: 'target/*.jar', onlyIfSuccessful: true
             build job: 'java_pipeline'
        }
    }   
} 
}
