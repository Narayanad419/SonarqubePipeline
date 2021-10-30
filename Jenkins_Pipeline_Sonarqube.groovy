OcurrentBuild.displayName="learndevops-#"+BUILD_NUMBER
pipeline {
    agent {
        label 'slave'
    }
    environment {
    PATH = "/opt/maven/bin:$PATH"
    }
    stages {
        stage('Hello') {
            steps {
                echo 'Hello World'
            }
        }
        stage('checkout') {
            steps {
               checkout([$class: 'GitSCM', branches: [[name: '*/master']], extensions: [], 
               userRemoteConfigs: [[url: 'https://github.com/Narayanad419/hello-world-war.git']]])
            }
        }
        stage('Build') {
            steps {
               sh 'mvn clean package'
            }
        }
        stage('SonarQube analysis') {
            steps {
         //withSonarQubeEnv('what name u gave at 'sonarqube servers' in 'configure systems' ')
	       withSonarQubeEnv('sonarQube') {
               sh "mvn sonar:sonar"
               }
            }
        }
        stage('Deploy') {
            steps {
               deploy adapters: [tomcat8(credentialsId: 'Tomcat', path: '', 
               url: 'http://13.232.48.23:8080/')], contextPath: 'helloworld', war: '**/target/*.war'
            }
        }
    }
}

