FROM jenkins/jenkins:2.266

# Install Maven
USER root

RUN apt-get update && \
  apt-get install -y maven

RUN mkdir /srv/backup && chown jenkins:jenkins /srv/backup

USER jenkins

COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt

ENV BACKUP_VERSION=FULL-2020-11-11_10-58

COPY --chown=jenkins:jenkins data/${BACKUP_VERSION} /var/jenkins_home