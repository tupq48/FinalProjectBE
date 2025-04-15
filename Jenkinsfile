pipeline {
    agent any

    stages {
        
        stage('Clone') {
            steps {
                git 'https://github.com/hieupham0906/FinalProjectBE.git'
            }
        }
        stage('Build') {
            steps {
            withDockerRegistry(credentialsId: 'dockerhub5', url: 'https://index.docker.io/v1/') {  
                    sh label: 'Build Docker Image', script: 'docker build -t chinhhieupham/testjenkins:latest .'
                    sh label: 'Push Docker Image', script: 'docker push chinhhieupham/testjenkins:latest'
                        
                }
            }
        }      
    }
}