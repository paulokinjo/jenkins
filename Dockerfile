FROM jenkins/jenkins:2.266

# Install Maven
USER root

ENV DOCKERVERSION=19.03.13

RUN apt-get update && \
  apt-get install -y maven curl

RUN curl -fsSLO https://download.docker.com/linux/static/stable/x86_64/docker-${DOCKERVERSION}.tgz \
  && tar xzvf docker-${DOCKERVERSION}.tgz --strip 1 \
  -C /usr/local/bin docker/docker \
  && rm docker-${DOCKERVERSION}.tgz

RUN curl -LO https://storage.googleapis.com/kubernetes-release/release/$(curl -s https://storage.googleapis.com/kubernetes-release/release/stable.txt)/bin/linux/amd64/kubectl \
  && chmod +x ./kubectl \
  && mv ./kubectl /usr/local/bin/kubectl

RUN mkdir /srv/backup && chown jenkins:jenkins /srv/backup

USER jenkins

COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt

ENV BACKUP_VERSION=FULL-2020-11-13_07-36

COPY --chown=jenkins:jenkins data/${BACKUP_VERSION} /var/jenkins_home