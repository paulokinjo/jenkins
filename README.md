# jenkins

## Open up a command prompt window.

### Create a bridge network in Docker using the following docker network create command:

<code>docker network create jenkins</code>

### Create the following volumes to share the Docker client TLS certificates needed to connect to the Docker daemon and persist the Jenkins data using the following docker volume create commands:

<code>docker volume create jenkins-docker-certs</code>
<br>
<code>docker volume create jenkins-data</code>

### In order to execute Docker commands inside Jenkins nodes, download and run the docker:dind Docker image using the following docker container run command:

<code>docker container run --name jenkins-docker --rm --detach ^
  --privileged --network jenkins --network-alias docker ^
  --env DOCKER_TLS_CERTDIR=/certs ^
  --volume jenkins-docker-certs:/certs/client ^
  --volume jenkins-data:/var/jenkins_home ^
  --volume "%HOMEDRIVE%%HOMEPATH%":/home ^
  docker:dind</code>

### Run the jenkinsci/blueocean image as a container in Docker using the following docker container run command (bearing in mind that this command automatically downloads the image if this hasn’t been done):

<code>docker container run --name jenkins-blueocean --rm --detach ^
  --network jenkins --env DOCKER_HOST=tcp://docker:2376 ^
  --env DOCKER_CERT_PATH=/certs/client --env DOCKER_TLS_VERIFY=1 ^
  --volume jenkins-data:/var/jenkins_home ^
  --volume jenkins-docker-certs:/certs/client:ro ^
  --volume "%HOMEDRIVE%%HOMEPATH%":/home ^
  --publish 8080:8080 --publish 50000:50000 jenkinsci/blueocean</code>
