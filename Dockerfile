FROM jenkins/jenkins:2.265

# Install Maven
USER root

RUN apt-get update && \
  apt-get install -y maven

RUN mkdir /srv/backup && chown jenkins:jenkins /srv/backup

USER jenkins

# Distributed Builds plugins
RUN /usr/local/bin/install-plugins.sh ssh-slaves

# UI
RUN /usr/local/bin/install-plugins.sh simple-theme-plugin

# Scaling
RUN /usr/local/bin/install-plugins.sh kubernetes

# Administration
RUN /usr/local/bin/install-plugins.sh thinBackup

ENV BACKUP_VERSION=FULL-2020-11-11_05-00

COPY --chown=jenkins:jenkins data/${BACKUP_VERSION} /var/jenkins_home