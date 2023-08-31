package com.alexsitiy.script.evaluation.service;

import com.alexsitiy.script.evaluation.exception.CapacityViolationException;
import com.alexsitiy.script.evaluation.model.Script;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * The implementation of  that uses {@linkplain } as
 * a representation of an evaluation and {@link String} as a script to evaluate JavaScript code.
 * <p/>
 * Utilizes {@link JSScriptRepository} to create and save the script.
 * Uses  to run scripts asynchronously.
 * This implementation also uses Spring Cache to prevent running the same scripts.
 *
 * @see com.alexsitiy.script.evaluation.model.Script
 * @see com.alexsitiy.script.evaluation.config.CachingConfiguration
 * @see org.springframework.cache.CacheManager
 */
@Service
public final class ScriptExecutionService {

    private final ScriptService scriptService;
    private final TaskExecutor taskExecutor;

    @Autowired
    public ScriptExecutionService(ScriptService scriptService,
                                  TaskExecutor taskExecutor) {
        this.scriptService = scriptService;
        this.taskExecutor = taskExecutor;
    }


    /**
     * Runs given JavaScript code in another Thread to evaluate the script. Utilizes {@link Cacheable}
     * for saving already passed script in order to prevent them from running one more time and consuming
     * system resources.
     *
     * @param jsCode JavaScript code passed for evaluation.
     * @return - as a representation of JavaScript code that contains
     * all the necessary information about it.
     * @see JSScriptRepository
     */

    public Script evaluate(String jsCode) {
        Script script = new Script(jsCode);

        try {
            CompletableFuture<Void> task = CompletableFuture
                    .runAsync(script, taskExecutor);
            script.setTask(task);
            scriptService.save(script);
        } catch (TaskRejectedException e) {
            throw new CapacityViolationException("There is no free space in the pool");
        }

        return script;
    }

    /**
     * Stops the script by its id.
     *
     * @param id id of the running script
     * @return true - if the script by id was found and stopped, false - if not.
     */
    public void stopById(Integer id) {
       scriptService.findById(id).stop();
    }

}
