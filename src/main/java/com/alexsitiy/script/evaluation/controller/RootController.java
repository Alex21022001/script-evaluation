package com.alexsitiy.script.evaluation.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * Root controller that returns HATEOAS links instead of 404 error.
 */
@Hidden
@RestController
@RequestMapping("/")
@SuppressWarnings("rawtypes")
public class RootController {

    @GetMapping
    public CollectionModel getRoot() {
        Link swaggerLink = Link.of(ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/swagger-ui/index.html").toUriString())
                .withRel("swagger")
                .withTitle("Gets the Swagger documentation");

        return CollectionModel.empty(swaggerLink);
    }
}
