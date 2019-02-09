import java.nio.file.Paths
import java.util.concurrent.TimeUnit
import static groovy.json.JsonOutput.*

def isWindows = System.getenv('OS').contains('Windows')
println "OS ${isWindows ? 'is' : 'is not'} Windows"

def webArtifactId = "${request.artifactId}-web"
println webArtifactId

def executables = [
        'npm': (isWindows ? 'npm.cmd' : 'npm'),
        'mvn': (isWindows ? 'mvn.cmd' : 'mvn'),
        'git': 'git'
]
println "executables:\n${printMap(executables)}"

def properties = request.properties
println printMap(properties)

def rootArtifactPath = "${Paths.get(request.outputDirectory, request.artifactId)}".toString()
def webArtifactPath = "${Paths.get(rootArtifactPath, webArtifactId)}".toString()

if (isTrue(properties.initGitRepo)) initGitRepo(executables, rootArtifactPath)
else println 'skipping git int...'
if (isTrue(properties.npmInstall)) runCommand([executables.npm, 'install'], webArtifactPath)
else println 'skipping npm install...'
if (isTrue(properties.installOnCreate)) runCommand([executables.mvn, 'install'], rootArtifactPath)
else println 'skipping mvn install...'

private void initGitRepo(LinkedHashMap<String, String> executables, String rootArtifactPath) {
    def git = executables.git
    runCommand([git, 'init'], rootArtifactPath)
    runCommand([git, 'add', '.'], rootArtifactPath)
}

def isTrue(val) {
    if (val instanceof Boolean) return val
    return Boolean.parseBoolean(val as String)
}

def runCommand(List strList, String directory, Map<String, String> envVars = [:]) {
    println(String.join(' ', strList))

    def proc = new ProcessBuilder(strList).directory(new File(directory))
    proc.environment().putAll(envVars)
    def result = proc
            .redirectOutput(ProcessBuilder.Redirect.INHERIT)
            .redirectError(ProcessBuilder.Redirect.INHERIT)
            .start()
    result.waitFor()
}

private String printMap(Map<String, String> executables) {
    return prettyPrint(toJson(executables))
}