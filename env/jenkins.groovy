#!groovy​

properties([pipelineTriggers([pollSCM('* * * * *')]), disableConcurrentBuilds(), buildDiscarder(logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '', daysToKeepStr: '', numToKeepStr: '10'))])

node {
    stage('checkout') {
        checkout $class: 'GitSCM',
                branches: [[name: '*/master']],
                extensions: [[$class: 'PathRestriction', excludedRegions: '', includedRegions: 'demo-service/.*']],
                userRemoteConfigs: [[credentialsId: 'build', url: 'https://github.com/neowu/core-ng-demo-project.git']]
    }
    stage('build') {
        sh "./gradlew check"
    }
}
