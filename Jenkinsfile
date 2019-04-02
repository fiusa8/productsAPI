#!groovy

// pipeline config
def javaAgent ='build && java'
def dockerAgent ='build && docker'
//def kubectlAgent ='deploy && datacenter && linux'
//def nodejsAgent ='build && nodejs'

// project config
// TODO: Provide the final project name as well as "trunk" branch to use and the environment tag for develop
def projectName = 'productsAPI-archetype'
def mainDevelopBranch = 'master'
def developEnvTag = 'master'
def version = '1.0'
def commit
def branch
def simplifiedBranchName
def repo

// git config
//TODO: Change for the credentials ID that will give access to the repo
//def gitCredentials = 'git-access'

// docker config
// TODO: update the details for the Docker registry to use and the credentials ID defined in Jenkins that will provide access to it
//def dockerCredentials = 'quay-access'
//def dockerRepo = 'tools.adidas-group.com:5000'
//def imagePrefix = 'adidas' //TODO: prefix of your image

// kubernetes config
//def kubernetesExecutionEnv = 'develop' // TODO: Define the execution environment (to load correct properties from config server)
//def k8sDeploymentYaml = "deploy/k8sDeployment.yaml"
//def k8sNamespace = "spring" //TODO: Configure project namespace in k8s space
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


@Library(['global-jenkins-library']) _

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
node(javaAgent) {
    /*properties([
            [$class: 'BuildDiscarderProperty', strategy: [$class: 'LogRotator', artifactDaysToKeepStr: artifactDaysToKeepStr, artifactNumToKeepStr: artifactNumToKeepStr, daysToKeepStr: daysToKeepStr, numToKeepStr: numToKeepStr]],
            parameters([
                    booleanParam(
                            defaultValue: true,
                            description: 'Do you want to destroy the temporal environment created after all steps completed?',
                            name: 'destroyEnvironmentAfter'
                    )
            ])
    ])*/

    try {
        stage('Collect info') {
            checkout scm

            /*branch = env.BRANCH_NAME
            commit = gitUtils.getCommitId()
            repo = gitUtils.getOriginUrl()
            if (branch == "${mainDevelopBranch}") {
                simplifiedBranchName = branch
            } else {
                simplifiedBranchName = gitUtils.getSimplifiedBranchName()
            }*/


            //slackUtils.notify message: "Building ${projectName} - ${branch}...", credentials: slackCredentials, team: slackTeam, channel: slackChannel
            //bitbucketUtils.notify message: "Collect info", commit: commit, status: 'progress', credentials: gitCredentials
        }

        stage('Automated Tests') {
            sh "mvn test"
        }

        stage('Build') {
            //bitbucketUtils.notify message: "Build", commit: commit, status: 'progress', credentials: gitCredentials
            try {
                sh "mvn clean package"
                /*artifactoryUtils.mavenDeploy //credentials: artifactoryCredentials,
                        //goal: 'clean org.jacoco:jacoco-maven-plugin:prepare-agent test install',
                        //releaseRepo: releaseRepo,
                        //snapshotRepo: snapshotRepo,
                        mavenTool: 'maven'*/
            } catch (Exception e) {
                //Ignored. Last Jenkins version has a bug that makes the Artifactory publish stage to fail with a exception
                // when the process works correctly so needs to be captured here while the bug is not solved.
            }
            stash 'workspace'
        }

        /*stage('Sonar') {

           bitbucketUtils.notify message: "Sonar", commit: commit, status: 'progress', credentials: gitCredentials
           sonar.run version: '1.0', branch: simplifiedBranchName

        }*/


        /*stage('Dockerize') {
            //bitbucketUtils.notify message: "Dockerize", commit: commit, status: 'progress', credentials: gitCredentials
            node(dockerAgent) {
                unstash 'workspace'
                dockerUtils.buildAndPush repo: dockerRepo,
                        image: "${imagePrefix}/${projectName}:${commit}",
                        credentials: dockerCredentials
            }
        }*/

        stage('Dockerize') {
            node(dockerAgent){
                unstash 'workspace'
                sh 'make dockerize'
            }
        }

        /*stage('Deploy-k8s') {
            bitbucketUtils.notify message: "Deploy-k8s", commit: commit, status: 'progress', credentials: gitCredentials
            node(kubectlAgent) {
                unstash 'workspace'
                withCredentials([file(credentialsId: k8sCredentials, variable: 'kubeconfigFile')]) {
                clusterBaseUrl = sh script: "kubectl --kubeconfig ${kubeconfigFile} config view --minify -o jsonpath='{.clusters[0].cluster.server}'", returnStdout: true
                clusterBaseUrl = clusterBaseUrl.replaceAll("https://api\\.", "")
                 echo "${clusterBaseUrl}"
                 def substitutionVariables = [
                            "DEPLOY_NAME=${simplifiedBranchName}-${projectName}",
                            "DEPLOY_IMAGE=${dockerRepo}/${imagePrefix}/${projectName}:${commit}",
                            "DEPLOY_SERVICE_HOSTNAME=${simplifiedBranchName}.${projectName}.${clusterBaseUrl}",
                            "K8S_NAMESPACE=${k8sNamespace}",
                            "EXECUTION_ENVIRONMENT=${kubernetesExecutionEnv}",
                            "CONFIG_SERVER_URI=${configServer}"
                    ]

                    kubectlUtils.updateOrCreateK8sDeployment substitutionVariables: substitutionVariables,
                            k8sDeploymentYaml: k8sDeploymentYaml,
                            pathToKubeconfigFile: kubeconfigFile,
                            namespace: k8sNamespace
                }
            }
        }*/

        /*stage("API Validation") {
            //TODO: Placeholder for the API validation step
            echo "Placeholder for the API validation step"
        }*/

        

        //If the test has been successful we will re-tag the image as :develop
        /*if (branch == mainDevelopBranch) {
            stage("Promote image") {
                //bitbucketUtils.notify message: "Promoting image to :develop", commit: commit, status: 'progress', credentials: gitCredentials
                node(dockerAgent) {
                    dockerUtils.retagImageAndPush repo: dockerRepo,
                            originalImage: "${imagePrefix}/${projectName}:${commit}",
                            newImage: "${imagePrefix}/${projectName}:${developEnvTag}",
                            credentials: dockerCredentials
                }
            }
        }*/

        /*if (params.destroyEnvironmentAfter && (branch != "${mainDevelopBranch}")) {
            stage('Undeploy-k8s') {
                node(kubectlAgent) {
                    withCredentials([file(credentialsId: k8sCredentials, variable: 'kubeconfigFile')]) {
                        kubectlUtils.completelyRemoveBranchDeployment deploymentName: "${simplifiedBranchName}-${projectName}",
                                pathToKubeconfigFile: kubeconfigFile,
                                namespace: k8sNamespace
                    }
                }
            }
            slackUtils.notify message: "Build success!", credentials: slackCredentials, team: slackTeam, channel: slackChannel
        } else {
            echo("Build success! - Service available in http://${simplifiedBranchName}-${projectName}/swagger-ui.html")
            slackUtils.notify message: "Build success! - Service available in http://${simplifiedBranchName}-${projectName}.${clusterBaseUrl}/swagger-ui.html",
                    credentials: slackCredentials, team: slackTeam, channel: slackChannel
        }

        bitbucketUtils.notify commit: commit, status: 'success', credentials: gitCredentials
        */

    } catch (def e) {
        /*stage('Roll-back') {
            node(kubectlAgent) {
                unstash 'workspace'
                withCredentials([file(credentialsId: k8sCredentials, variable: 'kubeconfigFile')]) {
                    if (branch != mainDevelopBranch) {
                        kubectlUtils.completelyRemoveBranchDeployment deploymentName: "${simplifiedBranchName}-${projectName}",
                                pathToKubeconfigFile: kubeconfigFile,
                                namespace: k8sNamespace
                    } else {
                        def substitutionVariables = [
                                "DEPLOY_NAME=${simplifiedBranchName}-${projectName}",
                                "DEPLOY_IMAGE=${dockerRepo}/${imagePrefix}/${projectName}:${developEnvTag}",
                                "DEPLOY_SERVICE_HOSTNAME=${simplifiedBranchName}.${projectName}.${clusterBaseUrl}",
                                "K8S_NAMESPACE=${k8sNamespace}",
                                "EXECUTION_ENVIRONMENT=${kubernetesExecutionEnv}",
                                "CONFIG_SERVER_URI=${configServer}"
                        ]

                        kubectlUtils.updateOrCreateK8sDeployment substitutionVariables: substitutionVariables,
                                k8sDeploymentYaml: k8sDeploymentYaml,
                                pathToKubeconfigFile: kubeconfigFile,
                                namespace: k8sNamespace
                    }
                }
            }
        }
        slackUtils.notify message: "Build success!", credentials: slackCredentials, team: slackTeam, channel: slackChannel, level: 'error'
        bitbucketUtils.notify commit: commit, status: 'error', credentials: gitCredentials
        currentBuild.result = 'FAILURE'
        */
    }
}