#!groovy​

properties([pipelineTriggers([pollSCM('H/5 * * * *')]), disableConcurrentBuilds(), buildDiscarder(logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '', daysToKeepStr: '', numToKeepStr: '10'))])

node {
    stage('checkout') {
        checkout $class: 'GitSCM',
                branches: [[name: '*/master']],
                extensions: [[$class: 'PathRestriction', excludedRegions: '', includedRegions: 'demo-service/.*']],
                userRemoteConfigs: [[credentialsId: 'build', url: 'git://test/test.git']]
    }
    stage('build') {
        sh "./gradlew check"
    }
}
