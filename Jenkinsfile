pipeline {
    agent any
    tools {
        gradle 'gradle'
    }
    environment {
        DATE = new Date().format('yy.M')
        TAG = "${DATE}.${BUILD_NUMBER}"
    }
    stages {
        stage('Build') {
            steps {
                sh 'cd ./devopslabs && gradle clean build'
            }
        }

        stage('Docker Build') {
            steps {
                script {
                    def dockerfilePath = '/home/ubuntu/jenkins/workspace/damrych_devopslabs_multi_main/devopslabs'

                    def dockerfile = "${dockerfilePath}/Dockerfile"

                    dir(dockerfilePath) {
                        docker.build("piotrdamrych/devopslabs:${TAG}", "-f ${dockerfile} .")
                    }
                }
            }
        }



        stage('Pushing Docker Image to Dockerhub') {
            when {
                branch "main"
            }
            steps {
                script {
                    docker.withRegistry('https://registry.hub.docker.com', 'devops-dockerhub') {
                        docker.image("piotrdamrych/devopslabs:${TAG}").push()
                        docker.image("piotrdamrych/devopslabs:${TAG}").push("latest")
                    }
                }
            }
        }
    }
}