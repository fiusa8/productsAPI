#!groovy

// pipeline config
def javaAgent ='build && java'
def dockerAgent ='build && docker'
def kubectlAgent ='deploy && datacenter && linux'
//def nodejsAgent ='build && nodejs'

// project config
// TODO: Provide the final project name as well as "trunk" branch to use and the environment tag for develop
def projectName = 'productsAPI'
def mainDevelopBranch = 'master'
def developEnvTag = 'master'
def version = '1.0'
def commit
def branch
def simplifiedBranchName
def repo

// git config
//TODO: Change for the credentials ID that will give access to the repo
def gitCredentials = ''

// docker config
// TODO: update the details for the Docker registry to use and the credentials ID defined in Jenkins that will provide access to it
//def dockerCredentials = 'quay-access'
//def dockerRepo = 'tools.adidas-group.com:5000'
//def imagePrefix = 'adidas' //TODO: prefix of your image

// kubernetes config
//def kubernetesExecutionEnv = 'default' // TODO: Define the execution environment (to load correct properties from config server)
def k8sDeploymentYaml = "deploy/k8sDeployment.yaml"
def k8sNamespace = "default" //TODO: Configure project namespace in k8s space
//def k8sCredentials = "kubeconfig" //TODO: Secret ID in Jenkins that contains the k8s kubeconfig file
//def clusterBaseUrl = ""

// Config server url
def configServer = 'http://config_server:8888'

// Slack configuration
// TODO: Add the credentials and slack details to send the notifications to the project designed channel
//def slackCredentials = 'slack-token'
//def slackTeam = 'slackTeam'
//def slackChannel = 'slackChannel'

//Artifactory configuration
// TODO: Provide the credentials and details for the Artifactory project were binaries will be stored
//def artifactoryCredentials = 'svc_XXX'
//def releaseRepo = 'releaseRepo'
//def snapshotRepo = 'snapshotRepo'


// Jenkins parameters for removing old builds
def artifactDaysToKeepStr = '2'
def artifactNumToKeepStr = '2'
def daysToKeepStr = '2'
def numToKeepStr = '2'


@Library(['global-jenkins-build']) _

/*node (javaAgent){
  try{
    stage('checkout project'){
        checkout scm
    }

    stage('check env'){
        sh "mvn -v"
        sh "java -version"
    }

    stage('test'){
        sh "mvn test"
    }

    stage('package'){
        sh "mvn package"
    }

    stage('report'){
        */
        //step([$class: 'JUnitResultArchiver', testResults: '**/target/surefire-reports/TEST-*.xml'])
    /*}

    stage('Artifact'){*/
        //step([$class: 'ArtifactArchiver', artifacts: '**/target/*.jar', fingerprint: true])
    /*}

  }catch(e){
    throw e
  }
}*/

// pipeline
pipeline {
    agent { node { label javaAgent } }

    options { skipDefaultCheckout true }

    stages {
        stage("Prepare") {
            steps {
                script {
                    fdUtils.loadGlobalLibrary()
                }
            }
        }

        stage("Checkout") {
            steps {
                script {
                    checkout scm

                    branch = env.BRANCH_NAME
                    commit = gitUtils.getCommitId()

                    stash 'workspace'
                }
            }
        }

        stage('Automated Tests') {
            steps {
                script {
                    mvnUtils.run goal: 'test'
                }
            }
        }

        stage('Build') {
            steps {
                script {
                    mvnUtils.run goal: 'clean package'
                }
            }
        }

        stage('Dockerize') {
            agent { node { label dockerAgent } }

            steps{
                script {
                    unstash 'workspace'
                    sh 'make dockerize'
                }
            }

        }

        stage('Deploy-k8s') {
            //bitbucketUtils.notify message: "Deploy-k8s", commit: commit, status: 'progress', credentials: gitCredentials
            agent { node { label kubectlAgent } }
            steps{
                script{
                    unstash 'workspace'
                    sh 'kubectl apply -f ${k8sDeploymentYaml}'
                    sh 'kubectl apply -f serviceDeployment.yaml'
                }
            }
                /*withCredentials([file(credentialsId: k8sCredentials, variable: 'kubeconfigFile')]) {
                clusterBaseUrl = sh script: "kubectl --kubeconfig ${kubeconfigFile} config view --minify -o jsonpath='{.clusters[0].cluster.server}'", returnStdout: true
                clusterBaseUrl = clusterBaseUrl.replaceAll("https://api\\.", "")
                 echo "${clusterBaseUrl}"
                 def substitutionVariables = [
                            "DEPLOY_NAME=${projectName}",
                            //"DEPLOY_IMAGE=${dockerRepo}/${imagePrefix}/${projectName}:${commit}",
                            //"DEPLOY_SERVICE_HOSTNAME=${simplifiedBranchName}.${projectName}.${clusterBaseUrl}",
                            "K8S_NAMESPACE=${k8sNamespace}",
                            "EXECUTION_ENVIRONMENT=${kubernetesExecutionEnv}",
                            "CONFIG_SERVER_URI=${configServer}"
                    ]

                    kubectlUtils.updateOrCreateK8sDeployment
                            k8sDeploymentYaml: k8sDeploymentYaml,
                            pathToKubeconfigFile: kubeconfigFile,
                            namespace: k8sNamespace
                            
                }*/
        }


    }
    post {
        success {
            script {
                currentBuild.result = 'SUCCESS'
            }
        }

        failure {
            script {
                currentBuild.result = 'FAILURE'
            }
        }
    }
}