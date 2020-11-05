import hudson.triggers.TimerTrigger
import jenkins.model.Jenkins
import groovy.json.JsonSlurper
import org.jenkinsci.plugins.workflow.job.WorkflowJob
import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition
import org.jenkinsci.plugins.scriptsecurity.scripts.ScriptApproval;

def instance = Jenkins.getInstance()
def home_dir = System.getenv("JENKINS_HOME")

def initialJobsConfigs = new JsonSlurper().parseText(new File("$home_dir/config/initial-jobs.json").text)

println("############################ STARTING INITIAL JOBS SETUP ############################")

initialJobsConfigs.each {
    println(">>> Remove ${it.name} seed job if already exists")

    def job = instance.getItem(it.name)
    if (job != null) {
        job.delete()
    }

    println(">>> Create ${it.name} seed job")

    def project = instance.createProject(WorkflowJob.class, it.name)
    project.setDefinition(new CpsFlowDefinition("""
pipeline {
    agent any 
    stages {
        stage('Build') { 
            steps {
                echo 'Building..'
            }
        }
        stage('Test') { 
            steps {
                echo 'Testing..'
            }
        }
        stage('Deploy') { 
            steps {
                echo 'deploying..'
            }
        }
    }
}

"""))

    project.addTrigger(new TimerTrigger('@midnight'))

    project.save()
}

instance.reload()

initialJobsConfigs.each {
    println ">>> Schedule ${it.name} seed jod"
    instance.getItem(it.name).scheduleBuild()
}

// Approve all pending scripts
def toApprove = ScriptApproval.get().getPendingScripts().collect()
toApprove.each { pending ->
    ScriptApproval.get().approveScript(pending.getHash())
}