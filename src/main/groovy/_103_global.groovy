import groovy.json.JsonSlurper
import jenkins.model.Jenkins
import jenkins.model.JenkinsLocationConfiguration

def home_dir = System.getenv("JENKINS_HOME")
def globalConfigs = new JsonSlurper().parseText(new File("$home_dir/config/global.json").text)

def helperFilePath = "$home_dir/init.groovy.d/helpers.groovy";
GroovyShell shell = new GroovyShell()
def helpers = shell.parse(new File(helperFilePath))

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

println(">>> Set system message ")
def env = System.getenv()
if ( env.containsKey('master_image_version') ) {
    // master_image_version set as env variable by the build process
    // Set it as a global variable in Jenkins to increase visibility
    helpers.addGlobalEnvVariable('master_image_version', env['master_image_version'])
    def date = new Date()
    def sdf = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss")
    systemMessage = "Este Jenkins foi configurado e provisionado via código.\n\n" +
            "Todas as alterações feitas de forma manual serão perdidas no próximo restart.\n " +
            "Alterações devem ser feitas em: ${globalConfigs.default_repo}\n\n" +
            "Jenkins-Docker Version: ${env['master_image_version']}\n" +
            "Deployment date: ${sdf.format(date)}\n\n"
    println "Set system message to:\n ${systemMessage}"
    Jenkins.instance.setSystemMessage(systemMessage)
} else {
    println "Can't set system message - missing env variable master_image_version"
}

println ">>> Set global env variables"

globalConfigs.environment_variables.each {
    println "Setting: ${it.key} = ${it.value}"
    helpers.addGlobalEnvVariable(it.key, it.value)
}