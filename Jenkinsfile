node {
    def DOCKERHUB_REPO = "sachinsharma31261/vehicle_reading_producer"
    def DOCKER_IMAGE_VERSION = "vehicle_reading_producer"

    stage("clean workspace") {
        deleteDir()
        echo DOCKERHUB_REPO
        echo DOCKER_IMAGE_VERSION
    }

    stage("git checkout") {
        checkout scm

        def GIT_COMMIT = sh(returnStdout: true, script: "git rev-parse HEAD").trim().take(7)
        DOCKER_IMAGE_VERSION = "${BUILD_NUMBER}-${GIT_COMMIT}"
    }

    stage("docker build") {
        sh "docker build . -t ${DOCKERHUB_REPO}:${DOCKER_IMAGE_VERSION} ."
    }

    stage("docker push") {
        withDockerRegistry(credentialsId: 'dockerhub') {
            sh "docker push ${DOCKERHUB_REPO}:${DOCKER_IMAGE_VERSION}"
        }
    }

    stage("Run container on server") {
        try {
        	sh "docker rm -f VehicleAlertProducer || true"
        	sh "docker run --env-file /prod.env -p 9040:9040 -d --name VehicleAlertProducer ${DOCKERHUB_REPO}:${DOCKER_IMAGE_VERSION}"
        }
        catch(e) {
			error "Docker Service Failed"
        }
        finally {
            sh "docker container prune -f"
            sh "docker image prune -af"
        }
    }
}