node{
  stage('SCM Checkout'){
  git 'https://github.com/BYTA-TECH/testDocker'
  }
  stage('Compile-Package'){
  sh 'mvn install -Dmaven.test.skip=true'
  }
   stage('Build Docker Image'){
       sh 'sudo mvn package -Pprod verify jib:dockerBuild -Dmaven.test.skip=true'
   }
  stage('Push Docker Image'){
      withCredentials([string(credentialsId: 'docker-pwd', variable: 'dockerHubPwd')]) {
          sh "docker login -u byta3262 -p ${dockerHubPwd}"
}
    sh 'sudo docker tag doctor byta3262/doctor:v1.0.2'
    sh 'sudo docker push byta3262/doctor'
  }
    //stage('run on dev-server'){
    //def dockerRun='docker-compose -f src/main/docker/app.yml up'
    //sshagent (credentials: ['dockerPwd']) {
        //sh "ssh -o StrictHostKeyChecking=no ec2-user@172.31.39.134 ${dockerRun}"
  //}
//}
        
}
