import com.cloudbees.plugins.credentials.domains.Domain
import com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl
import groovy.json.JsonSlurper
import jenkins.model.Jenkins
import com.cloudbees.jenkins.plugins.sshcredentials.impl.BasicSSHUserPrivateKey;
import com.cloudbees.plugins.credentials.CredentialsScope;

def global_domain = Domain.global()

def home_dir = System.getenv("JENKINS_HOME")
def credentialsConfigs = new JsonSlurper().parseText(new File("$home_dir/config/credentials.json").text)

def credentials_store = Jenkins.instance.getExtensionList('com.cloudbees.plugins.credentials.SystemCredentialsProvider')[0].getStore()

println("############################ STARTING CREDENTIALS CONFIG ############################")

credentialsConfigs.each {
    if (!new File(it.path).exists()) {
        throw new FileNotFoundException("${it.path} doesn't exists! Check credentials configuration")
    }

    switch (it.type) {
        case "ssh":
            println(">>> Create ssh credentials for user ${it.userId} with SSH private key ${it.path}")
            def creds = new BasicSSHUserPrivateKey(CredentialsScope.GLOBAL,
                    it.credentialsId,
                    it.userId,
                    new BasicSSHUserPrivateKey.FileOnMasterPrivateKeySource(it.path),
                    it.passphrase,
                    it.description)

            credentials_store.addCredentials(global_domain, creds)

            break

        case "password":
            println(">>> Create credentials for user ${it.userId} with the password from ${it.path}")
            def creds = new UsernamePasswordCredentialsImpl(CredentialsScope.GLOBAL,
                    it.credentialsId,
                    it.description,
                    it.userId,
                    new File(it.path).text.trim())

            credentials_store.addCredentials(global_domain, creds)
            break
        default:
            throw new UnsupportedOperationException("${it.type} credentials type is not supported!")
            break
    }
}
