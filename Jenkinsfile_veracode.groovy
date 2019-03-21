#!groovy

// pipeline config
def javaAgent ='build && java'

// project config
// TODO: Provide the final project name as well as "trunk" branch to use and the environment tag for develop
def projectName = 'productsAPI-seed'
def commit
def branch

// git config
//TODO: Change for the credentials ID that will give access to the repo
def gitCredentials = 'git-access'

// Slack configuration
// TODO: Add the credentials and slack details to send the notifications to the project designed channel
def slackCredentials = 'slack-token'
def slackTeam = 'adidascop'
def slackChannel = 'onboarding-comms'

// Veracode configuration
def sandobox = 'onboarding'
def veracodeCredentials = 'veracode-credentials'

// Jenkins parameters for removing old builds
def artifactDaysToKeepStr = '2'
def artifactNumToKeepStr = '2'
def daysToKeepStr = '2'
def numToKeepStr = '2'


@Library(['global-jenkins-library']) _

// pipeline
node(javaAgent) {
    properties([
            [$class: 'BuildDiscarderProperty', strategy: [$class: 'LogRotator', artifactDaysToKeepStr: artifactDaysToKeepStr, artifactNumToKeepStr: artifactNumToKeepStr, daysToKeepStr: daysToKeepStr, numToKeepStr: numToKeepStr]]
    ])

    try {
        stage('Collect info') {
            checkout scm
            branch = env.BRANCH_NAME
            commit = gitUtils.getCommitId()
            slackUtils.notify message: "Starting veracode analysis for ${projectName} - ${branch}...", credentials: slackCredentials, team: slackTeam, channel: slackChannel
        }

        stage('Build') {
            mvnUtils.run goal: 'clean package -U'
        }

        stage('Veracode') {
            veracodeUtils.javaStaticScan componentName: projectName,
                                        sandboxName: sandobox,
                                        scanName: "${projectName}-${commit}",
                                        credentials: veracodeCredentials
        }

        slackUtils.notify message: "Veracodeanalysis for ${projectName} - ${branch} completed successfuly", credentials: slackCredentials, team: slackTeam, channel: slackChannel

    } catch (def e) {
        slackUtils.notify message: "Veracode analysis for ${projectName} - ${branch} failed!", credentials: slackCredentials, team: slackTeam, channel: slackChannel, level: 'error'
        currentBuild.result = 'FAILURE'
    }
}