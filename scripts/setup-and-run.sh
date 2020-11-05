#!/usr/bin/env bash

dockerhub_user=paulokinjo
jenkins_port=8080
image_name=jenkins
image_version=2.0.0
container_name=md-jenkins

docker pull jenkins/jenkins:2.265

if [ ! -d downloads ]; then
  mkdir downloads
  curl -o downloads/jdk-8u144-linux-x64.tar.gz http://ftp.osuosl.org/pub/funtoo/distfiles/oracle-java/jdk-8u144-linux-x64.tar.gz
  curl -o downloads/jdk-7u80-linux-x64.tar.gz http://ftp.osuosl.org/pub/funtoo/distfiles/oracle-java/jdk-7u80-linux-x64.tar.gz
  curl -o downloads/apache-maven-3.5.2-bin.tar.gz http://mirror.vorboss.net/apache/maven/maven-3/3.5.2/binaries/apache-maven-3.5.2-bin.tar.gz
fi

docker stop ${container_name}
#docker build --no-cache -t ${dockerhub_user}/${image_name}:${image_version} ../
docker build -t ${dockerhub_user}/${image_name}:${image_version} ../

if [ ! -d m2deps ]; then
  mkdir m2deps
fi

if [ -d jobs ]; then
  rm -rf jobs
fi

if [ ! -d jobs ]; then
  mkdir jobs
fi

cp -r $HOME/.ssh $(pwd)

#    -v `pwd`/downloads:/var/jenkins_home/downloads \
#    -v `pwd`/jobs:/var/jenkins_home/jobs/ \
#    -v `pwd`/m2deps:/var/jenkins_home/.m2/repository/ \
#
#docker run -p ${jenkins_port}:8080 \
#    -v `pwd`/.ssh:/var/jenkins_home/.ssh/ \
#    --rm --name ${container_name} \
#    ${dockerhub_user}/${image_name}:${image_version}