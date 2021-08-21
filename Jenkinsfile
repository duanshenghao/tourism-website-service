def commit_msg
def isPushEvent = false
def tag = 'latest'
def description = ""
def build_time = ""
pipeline {
    agent any
    options {
        timestamps()
    }
    tools {
        maven "mvn-3.8.1"
    }
    stages {
        stage('init') {
           steps {
            script{
              def dockerPath = tool 'docker'
              env.PATH = "${dockerPath}/bin:${env.PATH}"
            }
           }
        }
        stage("Source Code") {
            steps {
                cleanWs()
                checkout([$class: 'GitSCM', branches: [[name: '*/dev']], extensions: [], userRemoteConfigs: [[credentialsId: 'duansh-gitlab', url: 'https://gitlab.eastbabel.cn/tourism/tourism-website-service.git']]])
                script {
                    commit_msg=sh(script: """set +x
  git log -1 --format=%s""", returnStdout:true)
                    def buildCause = currentBuild.getBuildCauses().toString()
                    println "buildCause: ${buildCause}"
                    isPushEvent = buildCause.contains('push')
                }
            }
        }
        stage("Input") {
            when {
                expression {
                    return !isPushEvent
                }
            }
            options {
                timeout(time:10, unit: "SECONDS")
            }
            steps {
                script {
                    try {
                        def inp = input id: 'Release', message: 'Release', parameters: [string(defaultValue: '', description: '', name: 'version', trim: false)]
                        tag = inp + ".${env.BUILD_NUMBER}"
                    } catch(e) {
                        tag = 'latest'
                    } finally {
                        println "tag: ${tag}"
                    }
                }
            }
        }
        stage("Package") {
            steps {
                sh "mvn package"
            }
        }
        stage("Build Image") {
            steps {
                script {
                    build_time=sh(script: """set +x
  date '+%Y年%m月%d日-%H:%M:%S'""", returnStdout:true)
                    build_time = build_time.replaceAll(/\n*$/, "")
                }
                sh """
                sed -i "s/{RELEASE_NO}/${tag}/g" Dockerfile
                sed -i "s/{BUILD_TIME}/${build_time}/g" Dockerfile
                docker build -t nexus.eastbabel.cn:8093/tourism-website-service:${tag} .
                """
            }
        }
        stage("Publish") {
            steps {
                withCredentials([usernamePassword(credentialsId: 'eastbabel-dockerhub', passwordVariable: 'password', usernameVariable: 'username')]) {
                    sh """
                     docker login nexus.eastbabel.cn:8093 -u ${username} -p ${password}
                     docker push nexus.eastbabel.cn:8093/tourism-website-service:${tag}
                     docker rmi nexus.eastbabel.cn:8093/tourism-website-service:${tag}
                    """
                }
            }
        }
        stage("Deploy to DEV") {
            when {
                expression {
                    return tag == null || 'latest'.equals(tag) || "".equals(tag.trim())
                }
            }
            steps {
                withCredentials([usernamePassword(credentialsId: 'eastbabel-server-dev', passwordVariable: 'password', usernameVariable: 'username')]) {
                    withCredentials([usernamePassword(credentialsId: 'eastbabel-dockerhub', passwordVariable: 'docker_password', usernameVariable: 'docker_username')]) {
                        sh """
                            sed -i "s/{TAG}/${tag}/g" docker-compose.yml
                            sshpass -p ${password} scp -oStrictHostKeyChecking=no docker-compose.yml ${username}@172.16.171.174:/docker/tourism-website-service/docker-compose.yml
                            sshpass -p ${password} ssh -oStrictHostKeyChecking=no ${username}@172.16.171.174 'cd /docker/tourism-website-service/ && docker login nexus.eastbabel.cn:8092 -u ${docker_username} -p ${docker_password} && docker-compose down && docker-compose pull && docker-compose up -d && docker image prune -f'
                            """
                    }
                }
            }
        }
    }
    post {
        success {
            script {
                currentBuild.description="${description}"
            }
        }
    }
}
