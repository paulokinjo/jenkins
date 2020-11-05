import groovy.json.JsonSlurper
import jenkins.model.Jenkins
import jenkins.model.JenkinsLocationConfiguration

def home_dir = System.getenv("JENKINS_HOME")
def globalConfigs = new JsonSlurper().parseText(new File("$home_dir/config/global.json").text)

println("############################ STARTING GLOBAL SETUP ############################")

println(">>> set number of executors on master to ${globalConfigs.numExecutorsOnMaster}")
Jenkins.instance.setNumExecutors(globalConfigs.numExecutorsOnMaster)

println(">>> set quite period to ${globalConfigs.scmQuietPeriod}")
Jenkins.instance.setQuietPeriod(globalConfigs.scmQuietPeriod)

println(">>> set checkout retry to ${globalConfigs.scmCheckoutRetryCount}")
Jenkins.instance.setScmCheckoutRetryCount(globalConfigs.scmCheckoutRetryCount)

// Change it to the DNS name if you have it
def jlc = JenkinsLocationConfiguration.get()
if (globalConfigs.jenkinsRootUrl) {
    println(">>> set jenkins root url to ${globalConfigs.jenkinsRootUrl}")
    jlc.setUrl(globalConfigs.jenkinsRootUrl)
} else {
    def ip = InetAddress.localHost.getHostAddress()
    println(">>> set jenkins root url to ${ip}")
    jlc.setUrl("http://$ip:8080")
}

// Set Admin Email as a string "Name <email>"
if (globalConfigs.jenkinsAdminEmail) {
    println ">>> set admin e-mail address to ${globalConfigs.jenkinsAdminEmail}"
    jlc.setAdminAddress(globalConfigs.jenkinsAdminEmail)
}

jlc.save()

println( ">>> Set Global GIT configuration name to ${globalConfigs.git.name} and email address to ${globalConfigs.git.email}")
def inst = Jenkins.getInstance()
def desc = inst.getDescriptor("hudson.plugins.git.GitSCM")
desc.setGlobalConfigName(globalConfigs.git.name)
desc.setGlobalConfigEmail(globalConfigs.git.email)