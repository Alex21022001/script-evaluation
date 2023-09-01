package com.alexsitiy.script.evaluation.service;

import com.alexsitiy.script.evaluation.exception.CapacityViolationException;
import com.alexsitiy.script.evaluation.exception.NoSuchScriptException;
import com.alexsitiy.script.evaluation.model.Script;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * This class is used for running and stopping scripts.
 * <p/>
 * Utilizes {@link ScriptService} to save and get the script.
 * Uses {@link TaskExecutor} to run scripts asynchronously.
 *
 * @see com.alexsitiy.script.evaluation.model.Script
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
     * Runs a given JavaScript code in a separate Thread to evaluate the script.
     * It also saves the script in the storage via {@link ScriptService}
     *
     * @param jsCode JavaScript code passed for evaluation.
     * @return {@link Script} as a representation of JavaScript code that holds
     * all the necessary information about it.
     * @throws CapacityViolationException if there is no free place in the thread pool.
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
     * Terminates the script by its id.
     *
     * @param id id of the running script.
     * @throws NoSuchScriptException if the script with a given id was not found.
     * @see ScriptService
     */
    public void stopById(Integer id) {
        scriptService.findById(id).stop();
    }

}
