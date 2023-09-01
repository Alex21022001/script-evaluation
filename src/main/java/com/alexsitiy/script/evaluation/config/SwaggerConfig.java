package com.alexsitiy.script.evaluation.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This configuration class is used for Swagger API configuration.
 * It includes configuration of info section.
 *
 * @see OpenAPI
 * */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Script Evaluation API")
                        .summary("REST API wrapper built with Spring Boot that integrates with the GraalJS JavaScript interpreter." +
                                 " It allows you to evaluate arbitrary JavaScript code through RESTful endpoints, providing features like non-blocking execution," +
                                 " script monitoring, and more.")
                        .description("""
                                Features:
                                - Evaluate arbitrary JavaScript code through RESTful API requests.
                                - Capture script output and errors during execution.
                                - View a list of scripts, their statuses, execution times, and other contextual information.
                                - Retrieve detailed information about a specific script, including its source code and console output/error.
                                - Terminate running or scheduled scripts by their ID to free up system resources.
                                - Remove inactive scripts (stopped, completed, failed) from the script list.
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Alex")
                                .email("alexsitiy@gmail.com"))
                );
    }
}