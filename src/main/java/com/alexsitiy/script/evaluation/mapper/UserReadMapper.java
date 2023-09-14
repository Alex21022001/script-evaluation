package com.alexsitiy.script.evaluation.mapper;

import com.alexsitiy.script.evaluation.controller.AuthController;
import com.alexsitiy.script.evaluation.dto.ScriptReadDto;
import com.alexsitiy.script.evaluation.dto.UserReadDto;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class UserReadMapper extends RepresentationModelAssemblerSupport<UserRepresentation, UserReadDto> {

    private final EntityLinks entityLinks;

    @Autowired
    public UserReadMapper(EntityLinks entityLinks) {
        super(AuthController.class, UserReadDto.class);
        this.entityLinks = entityLinks;
    }

    @Override
    protected UserReadDto instantiateModel(UserRepresentation entity) {
        return new UserReadDto(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getUsername(),
                entity.getEmail()
        );
    }

    @Override
    public UserReadDto toModel(UserRepresentation entity) {
        return instantiateModel(entity)
                .add(entityLinks.linkToCollectionResource(ScriptReadDto.class)
                        .withRel("scripts").withTitle("Gets all scripts"));
    }
}
