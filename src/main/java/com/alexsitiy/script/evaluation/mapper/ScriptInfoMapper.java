package com.alexsitiy.script.evaluation.mapper;

import com.alexsitiy.script.evaluation.dto.ScriptInfo;
import com.alexsitiy.script.evaluation.model.Script;
import org.springframework.stereotype.Component;

@Component
public class ScriptInfoMapper implements Mapper<Script, ScriptInfo> {

    @Override
    public ScriptInfo map(Script object) {
        return new ScriptInfo(
                object.getId(),
                object.getStatus(),
                object.getExecutionTime(),
                object.getScheduledTime()
        );
    }
}
