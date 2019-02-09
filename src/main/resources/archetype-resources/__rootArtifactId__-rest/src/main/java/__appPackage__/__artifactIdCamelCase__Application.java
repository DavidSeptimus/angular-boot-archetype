#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${appPackage};

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ${artifactIdCamelCase}Application {
    public static void main(String[] args) {
        SpringApplication.run(${artifactIdCamelCase}Application.class, args);
    }
}
