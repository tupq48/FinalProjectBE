pipeline {
    agent any

    
    environment {
        DOCKERHUB_CREDENTIALS = credentials('docker-hub1')
    } 
    stages {
        stage('Clone') {
            steps {
                git 'https://github.com/hieupham0906/FinalProjectBE.git'
            }
        }
        stage('Build') {
            steps {
                    sh label: 'Build Docker Image', script: 'docker build -t chinhhieupham/testjenkins:latest .'   
            }
        }
        stage('Login'){
            steps {
                script {
                        sh 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin'
                    }
            }

        }    
        stage('Push'){
            steps {
                sh 'docker push chinhhieupham/testjenkins:latest'
            }
        }  
    }
    post {
        always{
            script {
                sh 'docker logout'
            }
        }
    }
}