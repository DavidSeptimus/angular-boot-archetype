import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import org.apache.commons.io.FileUtils
import static groovy.json.JsonOutput.*


def isWindows = isTrue(System.properties['os.name']?.contains('Windows'))
println "OS ${isWindows ? 'is' : 'is not'} Windows"

executables = [
        'npm' : (isWindows ? 'npm.cmd' : 'npm'),
        'mvn' : (isWindows ? 'mvn.cmd' : 'mvn'),
        'git' : 'git',
        'ng'  : (isWindows ? 'ng.cmd' : 'ng'),
        'node': (isWindows ? 'node.exe' : 'node')
]
println "executables:\n${printMap(executables)}"

archProperties = request.properties
println printMap(archProperties)

rootArtifactPath = Paths.get(request.outputDirectory, request.artifactId).toString()
webArtifactId = "${request.artifactId}-web"
webArtifactPath = Paths.get(rootArtifactPath, webArtifactId).toString()
postGenerateResourcesPath = Paths.get(rootArtifactPath , '.post-generate-resources').toString()

// Post Generate Workflow
if (isTrue(archProperties.initGitRepo)) runCommand([executables.git, 'init'], rootArtifactPath)
else println 'skipping git int...'
println "initializing ${webArtifactId}..."

initAngularProject(rootArtifactPath)

cleanupPostGenerateResources()

runCommand([executables.git, 'add', '--all'], rootArtifactPath)

if (isTrue(archProperties.installOnCreate)) runCommand([executables.mvn, 'install'], rootArtifactPath)
else println 'skipping mvn install...'


private void initAngularProject(String root, String ngCliVersion = 'latest') {
    def npm = executables.npm
    def ng = executables.ng

    try{
        runCommand([ng, '--version'], webArtifactPath)
    } catch(any) {
        runCommand([npm, 'install', '-g', "@angular/cli@${ngCliVersion}".toString()], root)
    }

    def ngProperties = [
            'createApplication': isTrue(archProperties.createAngularApp),
            'appName'          : webArtifactId,
            'style'            : archProperties.style,
            'skipInstall'      : !isTrue(archProperties.npmInstall),
            'inlineTemplate'   : isTrue(archProperties.inlineTemplate),
            'inlineStyle'      : isTrue(archProperties.inlineStyle),
            'routing'          : isTrue(archProperties.routing)
    ]
    println printMap(ngProperties)

    runCommand([ng, 'new', ngProperties.appName,
                "--createApplication=${ngProperties.createApplication}",
                "--style=${ngProperties.style}",
                "--inlineStyle=${ngProperties.inlineStyle}",
                "--inlineTemplate=${ngProperties.inlineTemplate}",
                "--skipInstall=${ngProperties.skipInstall}",
                "--routing=${ngProperties.routing}",
                "--interactive=false",
                '--skipGit=true'
    ]*.toString() as List, root)
    // runCommand([npm, 'version', request.version], webArtifactPath)
    updatePackageJson()
}

void validateNgCliVersion(cliVersion, libVersion) {
    //ToDo: add real implementation (throw exception if validation fails)
}

def isTrue(val) {
    if (val instanceof Boolean) return val
    return Boolean.parseBoolean(val as String)
}

def runCommand(List strList, String directory, Map<String, String> envVars = [:]) {
    println String.join(' ', strList)

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

def updatePackageJson() {
    println 'updating package.json...'
    runCommand([executables.node,
                'update-package-json.js',
                "${Paths.get(webArtifactPath, 'package.json')}",
                request.version]*.toString(), postGenerateResourcesPath)
    println 'finished updating package.json'
}

def cleanupPostGenerateResources() {
    println "cleaning up ${postGenerateResourcesPath}..."
    FileUtils.forceDelete(new File(postGenerateResourcesPath))
    println 'cleanup complete'
}