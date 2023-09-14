package com.alexsitiy.script.evaluation.controller;

import com.alexsitiy.script.evaluation.dto.UserCreateDto;
import com.alexsitiy.script.evaluation.dto.UserReadDto;
import com.alexsitiy.script.evaluation.mapper.UserReadMapper;
import com.alexsitiy.script.evaluation.service.KeycloakService;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final KeycloakService keycloakService;
    private final UserReadMapper userMapper;

    @Autowired
    public AuthController(KeycloakService keycloakService,
                          UserReadMapper userMapper) {
        this.keycloakService = keycloakService;
        this.userMapper = userMapper;
    }

    @PostMapping
    public ResponseEntity<UserReadDto> register(@RequestBody
                                                @Validated
                                                UserCreateDto userCreateDto) {
        // TODO: 14.09.2023 Add HATEOAS to links
        // TODO: 14.09.2023 Add authorization
        // TODO: 14.09.2023 Add swagger auth
        // TODO: 14.09.2023 add documentation

        UserRepresentation userRepresentation = keycloakService.createUser(userCreateDto);
        UserReadDto user = userMapper.toModel(userRepresentation);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(user);
    }
}
