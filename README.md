# Jenkins AUTOMATION

# SSH KEY

<a href="https://www.ssh.com/ssh/keygen/">ssh-keygen - Generate a New SSH Key</a>

## What Is ssh-keygen?

<p>The SSH protocol uses public key cryptography for authenticating hosts and users. The authentication keys, called SSH keys, are created using the keygen program.</p>
<p>...</p>

## Creating an SSH Key Pair for User Authentication

```bash
ssh-keygen -t rsa -b 4096
```

# Create the password file

```bash
$ echo "MySecretPassword" > ~/.ssh/.password
```

# Run the script

<p>
Go to <b>scripts</b> folder.</p>
```bash
$ bash setup-and-run
```

# Run Docker

## Linux

```bash
docker run -p ${jenkins_port}:8080 \
    -v `pwd`/downloads:/var/jenkins_home/downloads \
    -v `pwd`/jobs:/var/jenkins_home/jobs/ \
    -v `pwd`/m2deps:/var/jenkins_home/.m2/repository/ \
    -v $HOME/.ssh:/var/jenkins_home/.ssh/ \
    --rm --name ${container_name} \
    ${dockerhub_user}/${image_name}:${image_version}
```

## Windows

Powershell

```bash
docker container run -v ${PWD}/.ssh:/var/jenkins_home/.ssh/ -v ${PWD}/downloads:/var/jenkins_home/downloads --rm --name ${container_name} -p ${jenkins_port}:8080 ${dockerhub_user}/${image_name}:${image_version}
```
