import hudson.model.FreeStyleProject
import hudson.model.ParameterDefinition
import hudson.model.ParametersDefinitionProperty
import hudson.model.StringParameterDefinition
import jenkins.model.Jenkins
import hudson.slaves.EnvironmentVariablesNodeProperty

def addGlobalEnvVariable(String key, String value) {
    def instance = Jenkins.getInstance()
    def globalNodeProperties = instance.getGlobalNodeProperties()
    def envVarsNodePropertyList = globalNodeProperties.getAll(hudson.slaves.EnvironmentVariablesNodeProperty.class)
    if (envVarsNodePropertyList == []) {
        EnvironmentVariablesNodeProperty.Entry entry = new EnvironmentVariablesNodeProperty.Entry(key, value);
        globalNodeProperties.add(new EnvironmentVariablesNodeProperty(entry))
    } else {
        envVarsNodePropertyList.get(0).getEnvVars().put(key, value)
    }
    println(">>> added global environment variable ${key} = ${value}")
    instance.save()
}

def addBuildParameter(FreeStyleProject job, String key, String value) {

    def param = new StringParameterDefinition(key, value, '')
    def parametersDefinitionProperty = job.getProperty(ParametersDefinitionProperty.class)

    if (parametersDefinitionProperty == null) {
        def params = new ArrayList<ParameterDefinition>(1)
        params.add(param)
        job.addProperty(new ParametersDefinitionProperty(params))
        println(">>> add job parameter ${key} = ${value} to the job ${job}")
    } else {
        if ((parametersDefinitionProperty.parameterDefinitions.find { it.name == key }) == null) {
            println(">>> add job parameter ${key} = ${value} to the job ${job}")
            parametersDefinitionProperty.parameterDefinitions.add(param)
        } else {
            println("${job} already have parameter ${key}. Skip it")
        }
    }
    job.save()
}
