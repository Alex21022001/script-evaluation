package com.alexsitiy.script.evaluation.controller;

import com.alexsitiy.script.evaluation.doc.annotation.RegisterUserApiEndpoint;
import com.alexsitiy.script.evaluation.dto.UserCreateDto;
import com.alexsitiy.script.evaluation.dto.UserReadDto;
import com.alexsitiy.script.evaluation.mapper.UserReadMapper;
import com.alexsitiy.script.evaluation.service.KeycloakService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This Controller is responsible for creating new Users via
 * Keycloak admin client.
 *
 * @see KeycloakService
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "Auth controller", description = "Contains endpoint for registration of a new User")
public class AuthController {

    private final KeycloakService keycloakService;
    private final UserReadMapper userMapper;

    @Autowired
    public AuthController(KeycloakService keycloakService,
                          UserReadMapper userMapper) {
        this.keycloakService = keycloakService;
        this.userMapper = userMapper;
    }

    /**
     * Creates a new User via a given {@link UserCreateDto} object, but
     * validates it first.
     *
     * @param userCreateDto - Dto for user's creation.
     * @return {@link UserReadDto} - is a representation of {@link UserRepresentation} object that
     * is obtained after a successful creation.
     * @see UserReadMapper
     */
    @PostMapping
    @RegisterUserApiEndpoint
    public ResponseEntity<UserReadDto> register(@RequestBody
                                                @Validated
                                                UserCreateDto userCreateDto) {

        UserRepresentation userRepresentation = keycloakService.createUser(userCreateDto);
        UserReadDto user = userMapper.toModel(userRepresentation);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(user);
    }
}
