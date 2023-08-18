package com.alexsitiy.script.evaluation.service;

import com.alexsitiy.script.evaluation.dto.JSScriptReadDto;
import com.alexsitiy.script.evaluation.mapper.JSScriptReadMapper;
import com.alexsitiy.script.evaluation.model.JSScript;
import com.alexsitiy.script.evaluation.thread.JSScriptTask;
import com.alexsitiy.script.evaluation.thread.ScriptThreadPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JSScriptExecutionService {

    private final JSScriptService jsService;
    private final ScriptThreadPool<JSScriptTask, Integer> threadPool;

    private final JSScriptReadMapper jsScriptReadMapper;

    @Autowired
    public JSScriptExecutionService(JSScriptService jsService,
                                    ScriptThreadPool<JSScriptTask, Integer> threadPool,
                                    JSScriptReadMapper jsScriptReadMapper) {
        this.jsService = jsService;
        this.threadPool = threadPool;
        this.jsScriptReadMapper = jsScriptReadMapper;
    }

    public JSScriptReadDto evaluate(String jsCode) {
        JSScript jsScript = jsService.create(jsCode);
        JSScriptTask jsScriptTask = new JSScriptTask(jsScript);
        threadPool.submit(jsScriptTask);
        return jsScriptReadMapper.map(jsScript);
    }
}
