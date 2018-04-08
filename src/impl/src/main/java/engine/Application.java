package engine;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.Collections;

/**
 * Main Spring Boot class
 */
@SpringBootApplication
@EnableAutoConfiguration
public class Application {

    public static void main(String[] args) throws Exception {
        new SpringApplicationBuilder(Application.class).
                properties(Collections.singletonMap("server.port", "8085"))
                .run();
    }

}
