## Angular Boot Archetype ##

#### Description ####
 angular-boot-archetype is a maven archetype for generating a multi-module project with separate Spring Boot and Angular modules 
 that are combined into a fat JAR file for easier distribution and deployment
 
 Spring security is included by default with a default configuration that disables security for all endpoints.
 
 The web module is generated dynamically using Angular CLI keep decouple the Angular module version from the Archetype
 version.
 
 #### Generated Submodules ####
 * ${artifact}-rest (Spring Boot)
 * ${artifact}-web (Angular)
 * ${artifact}-app (Runnable combined JAR)
 
 #### Prerequisite Components ####
 * Node.js
 * npm
 * Maven
 * JDK 8+
 
 #### Tested With ####
 * Windows 10 Pro
 * Maven: 3.6.0
 * Maven Archetype Plugin: 3.0.1
 * Node.js: 8.10.0
 * npm: 5.6.0
 * Angular CLI: 7.3.1
 
 ###### *Note: It should work on non-windows environments, but has not been tested* ######
 
 ### Simple Usage ###
 
1. run `mvn org.apache.maven.plugins:maven-archetype-plugin:3.0.1:generate  -Dfilter=com.dseptimus:angular-boot-archetype` from your terminal of choice
2. follow the prompts in your terminal

#### Configurable Properties #### 
* groupId\*: (Required)
* artifactId\*: (Required)
* version: 1.0.0-SNAPSHOT
* package: com.${groupId}
* appPackage: (dynamic - generally should not be changed)
* artifactIdCamelCase: (dynamic - generally should not be changed)
* createAngularApp: true (true|false)
* initGitRepo: true (true|false)
* inlineStyle: false (true|false)
* inlineTemplate: false (true|false)
* installOnCreate: true (true|false)
* npmInstall: true (true|false)
* routing: false (true|false)
* springBootStarterParentVersion: 2.1.2.RELEASE
* style: scss (css|scss|sass|less|styl)


#### Important Considerations ####
* This archetype is currently incompatible with the Intellij'a `New Project -> Maven -> Create from Achetype` wizard
due to its use of an outdated version of maven-archetype-plugin.
see: https://youtrack.jetbrains.com/issue/IDEA-206998
* In order for the post-generate groovy script tasks to run successfully, maven, git, and npm need to be included 
in your system's `PATH` environment variable