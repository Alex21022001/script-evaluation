package com.alexsitiy.script.evaluation.mapper;

import com.alexsitiy.script.evaluation.controller.ScriptRestController;
import com.alexsitiy.script.evaluation.dto.ScriptReadDto;
import com.alexsitiy.script.evaluation.model.Script;
import com.alexsitiy.script.evaluation.model.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * This class is a mapper that extends {@link RepresentationModelAssemblerSupport} and
 * converts {@link Script} to {@link ScriptReadDto}
 * also adds HATEOAS links to it.
 *
 * @see EntityLinks
 * @see org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
 */
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

    /**
     * Creates HATEOAS links to related resources. Utilizes toModel() method
     * to create an instance of {@link ScriptReadDto} and add a self link.
     *
     * @param entity {@link Script} that will be mapped to {@link ScriptReadDto} and complemented with HATEOAS links.
     * @return {@link ScriptReadDto} with links.
     * @see EntityLinks
     * @see WebMvcLinkBuilder
     */
    public ScriptReadDto toModelWithAllLinks(Script entity) {
        return toModel(entity)
                .add(entityLinks.linkForItemResource(MODEL_CLASS, entity.getId())
                        .slash("body").withSelfRel().withDeprecation("Gets the body of the script"))
                .add(entityLinks.linkForItemResource(MODEL_CLASS, entity.getId())
                        .slash("result").withSelfRel().withDeprecation("Gets the result of the script"))
                .add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CONTROLLER_CLASS)
                        .findAll(Set.of(Status.COMPLETED, Status.EXECUTING), List.of("TIME", "scheduled"))).withRel("allScripts"))
                .add(entityLinks.linkToItemResource(MODEL_CLASS, entity.getId())
                        .withRel("stop").withType("POST").withDeprecation("Terminates the running script"))
                .add(entityLinks.linkToItemResource(MODEL_CLASS, entity.getId())
                        .withRel("delete").withType("DELETE").withDeprecation("Deletes a finished script"));
    }

    /**
     * Gets a self Link as a string.
     *
     * @param id id to a specific resource.
     * @return {@link String} - representation of Link.
     */
    public String getSelfLink(Integer id) {
        return entityLinks.linkToItemResource(MODEL_CLASS, id).toString();
    }

    /**
     * Gets a Link to resources as a string.
     *
     * @return {@link String} - representation of Link.
     */
    public String getAllScriptsLink() {
        return entityLinks.linkToCollectionResource(CONTROLLER_CLASS).withRel("allScripts").toString();
    }
}
