pipeline {
  environment {
    registry = "ezsolution/share"
    registryCredential = '2affe7b4-f2a2-4282-afa1-df85a506a2ce'
    gitlabCredential = '39efad75-4e47-4ac7-8306-a4be2267b68e'
    dockerImage = ""
    serviceName = "admin-web"
  }
  agent any
  tools {
     maven 'Maven 3.3.9'
     jdk 'jdk8'
  }
  stages {
    stage('Cloning Git') {
      steps {
        git branch: 'baond', credentialsId: '02027319-c24b-441c-bbc2-0df6503cb824', url: 'https://gitlab.com/adsplus-vn/quotation/quotation-back-office.git'
      }
    }
    
    stage('Build jar') {
      steps {
        sh 'mvn -B -DskipTests clean package'
      }
    }

  }
}