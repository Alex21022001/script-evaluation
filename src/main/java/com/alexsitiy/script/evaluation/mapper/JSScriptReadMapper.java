package com.alexsitiy.script.evaluation.mapper;

import com.alexsitiy.script.evaluation.dto.JSScriptFullReadDto;
import com.alexsitiy.script.evaluation.dto.JSScriptReadDto;
import com.alexsitiy.script.evaluation.model.JSScript;
import org.springframework.stereotype.Component;

@Component
public class JSScriptReadMapper implements Mapper<JSScript, JSScriptReadDto> {

    @Override
    public JSScriptReadDto map(JSScript object) {
        return new JSScriptReadDto(
                object.getId(),
                object.getStatus(),
                object.calculateExecutionTime()
        );
    }
}
