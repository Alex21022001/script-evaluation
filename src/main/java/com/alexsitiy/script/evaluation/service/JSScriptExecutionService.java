package com.alexsitiy.script.evaluation.service;

import com.alexsitiy.script.evaluation.dto.JSScriptFullReadDto;
import com.alexsitiy.script.evaluation.mapper.JSScriptFullReadMapper;
import com.alexsitiy.script.evaluation.model.JSScript;
import com.alexsitiy.script.evaluation.repository.JSScriptRepository;
import com.alexsitiy.script.evaluation.thread.JSScriptTask;
import com.alexsitiy.script.evaluation.thread.ScriptThreadPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JSScriptExecutionService {

    private final JSScriptRepository jsScriptRepository;
    private final ScriptThreadPool<JSScriptTask, Integer> threadPool;

    private final JSScriptFullReadMapper jsScriptFullReadMapper;

    @Autowired
    public JSScriptExecutionService(JSScriptRepository jsScriptRepository,
                                    ScriptThreadPool<JSScriptTask, Integer> threadPool,
                                    JSScriptFullReadMapper jsScriptFullReadMapper) {
        this.jsScriptRepository = jsScriptRepository;
        this.threadPool = threadPool;
        this.jsScriptFullReadMapper = jsScriptFullReadMapper;
    }

    public JSScriptFullReadDto evaluate(String jsCode) {
        JSScript jsScript = jsScriptRepository.create(jsCode);
        JSScriptTask jsScriptTask = new JSScriptTask(jsScript);

        threadPool.submit(jsScriptTask);
        return jsScriptFullReadMapper.map(jsScript);
    }
}
