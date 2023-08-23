package com.alexsitiy.script.evaluation.service;

import com.alexsitiy.script.evaluation.dto.JSScriptFullReadDto;
import com.alexsitiy.script.evaluation.mapper.JSScriptFullReadMapper;
import com.alexsitiy.script.evaluation.model.JSScript;
import com.alexsitiy.script.evaluation.repository.JSScriptRepository;
import com.alexsitiy.script.evaluation.thread.task.JSScriptTask;
import com.alexsitiy.script.evaluation.thread.ScriptThreadPool;
import com.alexsitiy.script.evaluation.thread.task.ScriptTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class JSScriptExecutionService implements ScriptExecutionService<JSScriptFullReadDto, String> {

    private final ScriptThreadPool threadPool;
    private final JSScriptRepository jsScriptRepository;
    private final ApplicationEventPublisher eventPublisher;

    private final JSScriptFullReadMapper jsScriptFullReadMapper;

    @Autowired
    public JSScriptExecutionService(JSScriptRepository jsScriptRepository,
                                    ScriptThreadPool threadPool,
                                    ApplicationEventPublisher eventPublisher,
                                    JSScriptFullReadMapper jsScriptFullReadMapper) {
        this.jsScriptRepository = jsScriptRepository;
        this.threadPool = threadPool;
        this.eventPublisher = eventPublisher;
        this.jsScriptFullReadMapper = jsScriptFullReadMapper;
    }

    @Cacheable(cacheNames = "js-tasks", key = "#jsCode", sync = true)
    public JSScriptFullReadDto evaluate(String jsCode) {
        System.out.println("Invoked");
        JSScript jsScript = jsScriptRepository.create(jsCode);
        ScriptTask task = new JSScriptTask(jsScript, eventPublisher);

        threadPool.submit(task);
        return jsScriptFullReadMapper.map(jsScript);
    }

    @Override
    public <T extends Number> boolean stopById(T id) {
        return threadPool.stopTaskById(id);
    }

}
