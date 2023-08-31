package com.alexsitiy.script.evaluation.mapper;

import com.alexsitiy.script.evaluation.controller.ScriptRestController;
import com.alexsitiy.script.evaluation.dto.ScriptReadDto;
import com.alexsitiy.script.evaluation.model.Script;
import com.alexsitiy.script.evaluation.model.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ScriptReadMapper extends RepresentationModelAssemblerSupport<Script, ScriptReadDto> {

    private static final Class<ScriptRestController> CONTROLLER_CLASS = ScriptRestController.class;
    private static final Class<ScriptReadDto> MODEL_CLASS = ScriptReadDto.class;

    private final EntityLinks entityLinks;

    @Autowired
    public ScriptReadMapper(EntityLinks entityLinks) {
        super(ScriptRestController.class, ScriptReadDto.class);
        this.entityLinks = entityLinks;
    }

    @Override
    protected ScriptReadDto instantiateModel(Script entity) {
        return new ScriptReadDto(entity.getId(),
                entity.getStatus(),
                entity.getExecutionTime(),
                entity.getScheduledTime());
    }

    @Override
    public ScriptReadDto toModel(Script entity) {
        return createModelWithId(entity.getId(), entity);
    }

    public ScriptReadDto toModelWithAllLinks(Script entity) {
        return toModel(entity)
                .add(linkTo(methodOn(CONTROLLER_CLASS)
                        .getBody(entity.getId(),null)).withSelfRel().withDeprecation("Gets the body of the script"))
                .add(linkTo(methodOn(CONTROLLER_CLASS)
                        .getResult(entity.getId(),null)).withSelfRel().withDeprecation("Gets the result of the running script"))
                .add(linkTo(methodOn(CONTROLLER_CLASS)
                        .findAll(Set.of(Status.COMPLETED, Status.EXECUTING), List.of("TIME", "scheduled"))).withRel("allScripts"))
                .add(entityLinks.linkToItemResource(MODEL_CLASS, entity.getId())
                        .withRel("stop").withType("POST").withDeprecation("Terminates the running script"))
                .add(entityLinks.linkToItemResource(MODEL_CLASS, entity.getId())
                        .withRel("delete").withType("DELETE").withDeprecation("Deletes a finished script"));
    }
}
