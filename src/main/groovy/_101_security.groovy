import groovy.json.JsonSlurper
import hudson.model.Descriptor
import jenkins.model.Jenkins

def home_dir = System.getenv("JENKINS_HOME")
def authenticationConfigs = new JsonSlurper().parseText(new File("$home_dir/config/authentication.json").text)

// JobDSL security is enabled by default
if (!authenticationConfigs.scriptSecurity.jobDsl) {
    // Read more here https://github.com/jenkinsci/job-dsl-plugin/wiki/Migration#migrating-to-160
    println(">>> Attention! Disabling script security for JobDSL")
    Descriptor dslSecurity = Jenkins.instance.getDescriptor('javaposse.jobdsl.plugin.GlobalJobDslSecurityConfiguration')
    if (dslSecurity != null) {
        dslSecurity.setUseScriptSecurity(authenticationConfigs.scriptSecurity.jobDsl)
        Jenkins.instance.save()
    } else {
        println("---> Warn: Please check if the following plugin is installed GlobalJobDslSecurityConfiguration")
    }
}
