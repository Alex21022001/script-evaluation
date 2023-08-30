package com.alexsitiy.script.evaluation.mapper;

import com.alexsitiy.script.evaluation.controller.ScriptRestController;
import com.alexsitiy.script.evaluation.dto.ScriptReadDto;
import com.alexsitiy.script.evaluation.model.Script;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class ScriptReadMapper extends RepresentationModelAssemblerSupport<Script, ScriptReadDto> {

    public ScriptReadMapper() {
        super(ScriptRestController.class, ScriptReadDto.class);
    }

    @Override
    protected ScriptReadDto instantiateModel(Script entity) {
        return new ScriptReadDto(entity.getId(),
                entity.getStatus(),
                entity.getExecutionTime(),
                entity.getScheduledTime(),
                entity.getBody(),
                entity.getResult().toString());
    }

    @Override
    public ScriptReadDto toModel(Script entity) {
        return createModelWithId(entity.getId(), entity);
    }
}
