FROM jenkins/jenkins:2.265

LABEL Author="Paulo Kinjo"

ARG master_image_version="v.2.0.0"
ENV master_image_version $master_image_version

USER root
RUN apt-get update && apt-get install -y make git openjdk-8-jdk

USER jenkins

COPY ./scripts/plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt

COPY ./src/main/groovy/* /usr/share/jenkins/ref/init.groovy.d/
COPY ./src/main/resources/*.json ${JENKINS_HOME}/config/
