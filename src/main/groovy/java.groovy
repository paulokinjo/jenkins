import groovy.json.JsonSlurper
import hudson.model.JDK
import hudson.tools.InstallSourceProperty
import hudson.tools.ZipExtractionInstaller

def descriptor = new JDK.DescriptorImpl();

def List<JDK> installations = []

def home_dir = System.getenv("JENKINS_HOME")
def toolsConfig = new JsonSlurper().parseText(new File("$home_dir/config/tools.json").text)

println "############################ STARTING JDKs CONFIG ############################"

toolsConfig.each {
    if (it.enabled) {
        println(">>> Setting up tool: ${it.name}")

        def installer = new ZipExtractionInstaller('master',
                it.url,
                it.subdir)
        def jdk = new JDK(it.name as String, null, [new InstallSourceProperty([installer])])
        installations.add(jdk)
    }
}
descriptor.setInstallations(installations.toArray(new JDK[installations.size()]))
descriptor.save()
