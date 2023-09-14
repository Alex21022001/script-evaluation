package com.alexsitiy.script.evaluation.mapper;

import com.alexsitiy.script.evaluation.dto.UserCreateDto;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class UserRepresentationMapper {

    public UserRepresentation map(UserCreateDto userCreateDto) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setFirstName(userCreateDto.getFirstname());
        userRepresentation.setLastName(userCreateDto.getLastname());
        userRepresentation.setUsername(userCreateDto.getUsername());
        userRepresentation.setEmail(userCreateDto.getEmail());
        userRepresentation.setCredentials(convertToCredentials(userCreateDto.getPassword()));

        userRepresentation.setEnabled(true);
        userRepresentation.setEmailVerified(false);

        return userRepresentation;
    }

    private List<CredentialRepresentation> convertToCredentials(String password) {
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue(password);
        credentialRepresentation.setTemporary(false);

        return Collections.singletonList(credentialRepresentation);
    }
}
