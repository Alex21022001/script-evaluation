package com.alexsitiy.script.evaluation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.hateoas.config.EnableHypermediaSupport;

@SpringBootApplication
@EnableHypermediaSupport(type = {EnableHypermediaSupport.HypermediaType.HAL, EnableHypermediaSupport.HypermediaType.HAL_FORMS})
@EnableConfigurationProperties
public class ScriptEvaluationApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScriptEvaluationApplication.class, args);
    }

}
