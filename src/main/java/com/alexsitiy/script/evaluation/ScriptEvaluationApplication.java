package com.alexsitiy.script.evaluation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.hateoas.config.EnableHypermediaSupport;

@SpringBootApplication
@EnableHypermediaSupport(type = {EnableHypermediaSupport.HypermediaType.HAL, EnableHypermediaSupport.HypermediaType.HAL_FORMS})
public class ScriptEvaluationApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScriptEvaluationApplication.class, args);
    }

}
