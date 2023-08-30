package com.alexsitiy.script.evaluation.mapper;

import com.alexsitiy.script.evaluation.dto.ScriptReadDto;
import com.alexsitiy.script.evaluation.model.Script;
import org.springframework.stereotype.Component;

@Component
public class ScriptReadMapper implements Mapper<Script, ScriptReadDto> {

    @Override
    public ScriptReadDto map(Script object) {
        return new ScriptReadDto(object.getId(),
                object.getStatus(),
                object.getExecutionTime(),
                object.getScheduledTime(),
                object.getBody(),
                object.getResult().toString());
    }
}
