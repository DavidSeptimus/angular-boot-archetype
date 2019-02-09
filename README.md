## Angular Boot Archetype ##

#### Description ####
 angular-boot-archetype is a maven archetype for generating a multi-module project with separate Spring Boot and Angular modules 
 that are combined into a fat JAR file for easier distribution and deployment
 
 Spring security is included by default with a default configuration that disables security for all endpoints.
 
 #### Generated Submodules ####
 * ${artifact}-rest (Spring Boot)
 * ${artifact}-web (Angular)
 * ${artifact}-app (Runnable combined JAR)
 
 #### Tested With ####
 * Windows 10 Pro
 * Maven 3.6.0
 * Maven Archetype Plugin 3.0.1
 * Node.js v8.10.0
 * npm 5.6.0
 * Angular 7
 
 ###### Note: It should work on non-windows environments, but has not been tested ######
 
 ### Simple Usage ###
 
1. run `mvn org.apache.maven.plugins:maven-archetype-plugin:3.0.1:generate` from your terminal of choice
2. follow the prompts in your terminal

#### Important Considerations ####
* This archetype is currently incompatible with the Intellij'a `New Project -> Maven -> Create from Achetype` wizard
due to its use of an outdated version of maven-archetype-plugin.
see: https://youtrack.jetbrains.com/issue/IDEA-206998
* In order for the post-generate groovy script tasks to run successfully, maven, git, and npm need to be included 
in your system's `PATH` environment variable