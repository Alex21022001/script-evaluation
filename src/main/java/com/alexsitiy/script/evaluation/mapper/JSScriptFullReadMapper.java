package com.alexsitiy.script.evaluation.mapper;

import com.alexsitiy.script.evaluation.dto.JSScriptFullReadDto;
import com.alexsitiy.script.evaluation.model.JSScript;
import org.springframework.stereotype.Component;

@Component
public class JSScriptFullReadMapper implements Mapper<JSScript, JSScriptFullReadDto> {

    @Override
    public JSScriptFullReadDto map(JSScript object) {
        return new JSScriptFullReadDto(
                object.getId(),
                object.getStatus(),
                object.calculateExecutionTime(),
                object.getBody(),
                object.readResult(),
                object.getErrors()
        );
    }
}
