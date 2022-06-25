echo "---build start---"

pipeline {
    agent any
    environment {
       SPRING_DATASOURCE_URL = "jdbc:mysql://mysql-db/yousinsa"
       DATASOURCE_USERNAME = "root"
       DATASOURCE_PASSWORD = "password"
    }

    stages
    {
        stage('Git Pull Stage') {
            steps {
                sh 'echo check'
                git branch: 'master', credentialsId: 'jenkins-ssh', url: 'https://github.com/f-lab-edu/yousinsa.git'
            }
        }

        stage('Build Stage') {
            steps {
                sh "./gradlew clean bootJar"
            }
        }

        stage('Run Unit tests') {
            steps {
                sh "./gradlew test"
                junit '**/build/test-results/test/*.xml'
            }
        }

        stage('Deploy Stage') {
            steps {
                sh 'sudo /usr/local/bin/docker-compose build'
                sh "sudo docker image tag rlfbd5142/yousinsa:latest rlfbd5142/yousinsa:0.0.${env.BUILD_NUMBER}"
                sh "sudo docker push rlfbd5142/yousinsa:0.0.${env.BUILD_NUMBER}"
            }
        }

        stage('Run Docker Stage') {
            steps {
                sh 'sudo docker ps -f name=yousinsa -q | xargs --no-run-if-empty sudo docker container stop'
                sh 'sudo docker container ls -a -f name=yousinsa -q | xargs -r sudo docker container rm'
                sh "sudo docker run -v /var/run/docker.sock:/var/run/docker.sock docker run -d --name yousinsa -p 8081:8080 --env SPRING_DATASOURCE_URL=jdbc:mysql://mysql-db/yousinsa --env DATASOURCE_USERNAME=root --env DATASOURCE_PASSWORD=password --network yousinsa-net rlfbd5142/yousinsa:0.0.${env.BUILD_NUMBER}"
            }
        }
    }
}


