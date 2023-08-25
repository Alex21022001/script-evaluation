package com.alexsitiy.script.evaluation.mapper;

import com.alexsitiy.script.evaluation.dto.JSScriptReadDto;
import com.alexsitiy.script.evaluation.model.JSScript;
import org.springframework.stereotype.Component;

/**
 *  The class implements {@link Mapper} interface to map {@link JSScript} to {@link JSScriptReadDto}.
 *  It also has {@link Component} annotation in order to create a Spring Bean of this class and inject is
 *  via DI.
 * */
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
