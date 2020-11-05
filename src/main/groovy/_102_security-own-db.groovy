import groovy.json.JsonSlurper
import hudson.security.HudsonPrivateSecurityRealm
import jenkins.model.Jenkins

def home_dir = System.getenv("JENKINS_HOME")
def authenticationConfigs = new JsonSlurper().parseText(new File("$home_dir/config/authentication.json").text)

if (authenticationConfigs.owndb.enabled) {
    println "############################ STARTING INTERNAL DATABASE SECURITY SETUP ############################"

    def realm = new HudsonPrivateSecurityRealm(false)
    authenticationConfigs.owndb.users.each {
        def passwordFile = new File(it.path)
        realm.createAccount(it.userId, passwordFile.text.trim())
        println(">>> Added user ${it.userId}")
    }

    Jenkins.instance.setSecurityRealm(realm)
    Jenkins.instance.save()
}