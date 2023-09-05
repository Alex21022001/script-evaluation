package com.alexsitiy.script.evaluation.controller;

import com.alexsitiy.script.evaluation.controller.ScriptRestController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

/**
 * Root controller that returns HATEOAS links instead of 404 error.
 */
@RestController
@RequestMapping("/")
@SuppressWarnings("rawtypes")
public class RootController {

    @GetMapping
    public CollectionModel getRoot() {
        Link scriptsLink = linkTo(ScriptRestController.class)
                .withRel("scripts").withDeprecation("Gets all scripts")
                .andAffordance(afford(methodOn(ScriptRestController.class).evaluate(null)));

        Link swaggerLink = Link.of(ServletUriComponentsBuilder.fromCurrentRequest()
                        .replacePath("/swagger-ui.html").toUriString())
                .withRel("swagger")
                .withDeprecation("Gets the Swagger documentation");

        return CollectionModel.empty(scriptsLink, swaggerLink);
    }
}
