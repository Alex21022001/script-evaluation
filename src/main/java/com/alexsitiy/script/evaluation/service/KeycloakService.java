package com.alexsitiy.script.evaluation.service;

import com.alexsitiy.script.evaluation.dto.UserCreateDto;
import com.alexsitiy.script.evaluation.exception.InvalidUserDataException;
import com.alexsitiy.script.evaluation.mapper.UserRepresentationMapper;
import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class KeycloakService {

    private static final String DEFAULT_ROLE = "user";

    private final Logger log = LoggerFactory.getLogger(KeycloakService.class);

    private final Keycloak keycloak;
    private final String realm;
    private final UserRepresentationMapper userMapper;

    private RealmResource realmResource;
    private UsersResource usersResource;

    @Autowired
    public KeycloakService(@Value("${keycloak.realm}") String realm, Keycloak keycloak, UserRepresentationMapper userMapper) {
        this.realm = realm;
        this.keycloak = keycloak;
        this.userMapper = userMapper;
    }

    @PostConstruct
    public void init() {
        realmResource = keycloak.realm(realm);
        usersResource = realmResource.users();
    }

    public UserRepresentation createUser(UserCreateDto user) {
        UserRepresentation userRepresentation = userMapper.map(user);
        Response response = usersResource.create(userRepresentation);

        if (response.getStatus() == 409) {
            log.debug("Couldn't create the user by provided data");
            throw new InvalidUserDataException("Couldn't create the user by provided data");
        }

        String userId = CreatedResponseUtil.getCreatedId(response);
        userRepresentation.setId(userId);
        log.debug("User was created successfully. User id:{}", userId);
        addDefaultRole(userId);

        return userRepresentation;
    }

    private void addDefaultRole(String userId) {
        RoleRepresentation roleRepresentation = realmResource.roles().get(DEFAULT_ROLE).toRepresentation();
        UserResource user = usersResource.get(userId);
        user.roles().realmLevel().add(Collections.singletonList(roleRepresentation));
        log.debug("Default role:{} was set to user with id:{}", DEFAULT_ROLE, userId);
    }


}
