# Jenkins Auto Scaling on top of Kubernetes

# Generating Jenkins Master Image

First check the /data directory, to identify which backup the jenkins master server must restore.

Second check the Dockerfile and update the "BACKUP_VERSION" env variable accordingly.

Finally:

```
$ docker build -t paulokinjo/jenkins:1.0.0 .
```

# Kubernetes

## Dashboard

Go to /kubernetes/dashboard directory

```
$ kubectl apply -f service-account.yml
```

```
$ kubectl apply -f cluster-role-binding.yml
```

Install the Dashboard application into the cluster

```
$ kubectl apply -f https://raw.githubusercontent.com/kubernetes/dashboard/v2.0.0-rc6/aio/deploy/recommended.yaml
```

Get the Token for the ServiceAccount

```
$ kubectl -n kubernetes-dashboard describe secret $(kubectl -n kubernetes-dashboard get secret | grep admin-user | awk '{print $1}')
```

Start a kubectl proxy

```
$ kubectl proxy
```

Enter the URL on your browser:

```
http://localhost:8001/api/v1/namespaces/kubernetes-dashboard/services/https:kubernetes-dashboard:/proxy/
```

# Jenkins

## Master

Go to /kubernetes/jenkins

```
$ kubectl apply -f jenkins-service.yml
```

```
$ kubectl apply -f jenkins-deployment.yml
```

Get the jenkins service port

```
$ kubectl describe service jenkins
```

```
Name:                     jenkins
Namespace:                default
Labels:                   <none>
Annotations:              <none>
Selector:                 app=jenkins
Type:                     NodePort
IP:                       10.102.148.47
LoadBalancer Ingress:     localhost
Port:                     <unset>  8080/TCP
TargetPort:               8080/TCP
NodePort:                 <unset>  30080/TCP
Endpoints:                10.1.0.80:8080
Session Affinity:         None
External Traffic Policy:  Cluster
```

Access the NodePort: http://localhost:30080

# Credentials

jenkins:jenkins

## Configuring Kubernetes

Go to http://localhost:32123/configureClouds/

Check if the Kubernetes URL matches:

```
$ kubectl cluster-info | grep master
```

Check if the Jenkins URL matches the end point for the service above:

```
Endpoints:                10.1.0.80:8080
```

All Done.

# Backup

Access ThinBackup

Once the backup is generated go to <b>kubernetes/scripts</b> directory and run the following:

```
$ bash copy-jenkins-backup.sh <POD_NAME>
```

This will copy the generated backup to the <b>/data</b> directory

# References

<a href="https://www.blazemeter.com/blog/how-to-setup-scalable-jenkins-on-top-of-a-kubernetes-cluster/">
How to Setup Scalable Jenkins on Top of a Kubernetes Cluster
</a>
