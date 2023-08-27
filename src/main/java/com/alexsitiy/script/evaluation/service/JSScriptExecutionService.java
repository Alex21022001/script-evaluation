package com.alexsitiy.script.evaluation.service;

import com.alexsitiy.script.evaluation.dto.JSScriptFullReadDto;
import com.alexsitiy.script.evaluation.mapper.JSScriptFullReadMapper;
import com.alexsitiy.script.evaluation.model.Script;
import com.alexsitiy.script.evaluation.repository.JSScriptRepository;
import com.alexsitiy.script.evaluation.thread.ScriptThreadPool;
import com.alexsitiy.script.evaluation.thread.task.ScriptTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 *  The implementation of  that uses {@linkplain JSScriptFullReadDto} as
 *  a representation of an evaluation and {@link String} as a script to evaluate JavaScript code.
 *  <p/>
 *  Utilizes {@link JSScriptRepository} to create and save the script.
 *  Uses {@link ScriptThreadPool} to run scripts asynchronously.
 *  This implementation also uses Spring Cache to prevent running the same scripts.
 *
 * @see ScriptTask
 * @see com.alexsitiy.script.evaluation.model.Script
 * @see com.alexsitiy.script.evaluation.config.CachingConfiguration
 * @see org.springframework.cache.CacheManager
 * */
@Service
public class JSScriptExecutionService {

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

    /**
     *  Runs given JavaScript code in another Thread to evaluate the script. Utilizes {@link Cacheable}
     *  for saving already passed script in order to prevent them from running one more time and consuming
     *  system resources.
     *
     * @param jsCode JavaScript code passed for evaluation.
     * @return {@link JSScriptFullReadDto} - as a representation of JavaScript code that contains
     * all the necessary information about it.
     * @see ScriptThreadPool
     * @see JSScriptRepository
     * */

    public Script evaluate(String jsCode) {
        Script script = new Script(jsCode);
        // TODO: 27.08.2023 save script
        return script;
    }

    /**
     *  Stops the script by its id.
     *
     * @param id id of the running script
     * @return true - if the script by id was found and stopped, false - if not.
     * */
    public void stopById(Integer id){

    }

}
