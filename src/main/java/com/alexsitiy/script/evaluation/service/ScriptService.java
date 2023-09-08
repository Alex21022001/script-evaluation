package com.alexsitiy.script.evaluation.service;

import com.alexsitiy.script.evaluation.exception.CapacityViolationException;
import com.alexsitiy.script.evaluation.exception.IllegalScriptStateException;
import com.alexsitiy.script.evaluation.exception.NoSuchScriptException;
import com.alexsitiy.script.evaluation.model.Script;
import com.alexsitiy.script.evaluation.model.Status;
import com.alexsitiy.script.evaluation.repository.ScriptRepository;
import io.micrometer.core.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * This class is used for running and stopping scripts.
 * <p/>
 * Utilizes {@link ScriptRepository} to save and get the script.
 * Uses {@link TaskExecutor} to run scripts asynchronously.
 *
 * @see com.alexsitiy.script.evaluation.model.Script
 */
@Service
public class ScriptService {

    private final ScriptRepository scriptRepository;
    private final TaskExecutor taskExecutor;

    @Autowired
    public ScriptService(ScriptRepository scriptRepository,
                         TaskExecutor taskExecutor) {
        this.scriptRepository = scriptRepository;
        this.taskExecutor = taskExecutor;
    }

    /**
     * Delegates method to {@link ScriptRepository}
     */
    @Timed("script.findAll")
    public List<Script> findAll(Set<Status> statuses, List<String> sorts) {
        return scriptRepository.findAll(statuses, sorts);
    }

    /**
     * Delegates method to {@link ScriptRepository}
     */
    @Timed("script.findById")
    public Script findById(Integer id) {
        return scriptRepository.findById(id);
    }


    /**
     * Runs a given JavaScript code in a separate Thread to evaluate the script,
     * but before it checks for syntax errors.
     * It also saves the script in the storage via {@link ScriptRepository}
     *
     * @param jsCode JavaScript code passed for evaluation.
     * @return {@link Script} as a representation of JavaScript code that holds
     * all the necessary information about it.
     * @throws CapacityViolationException if there is no free place in the thread pool.
     */
    public Script evaluate(String jsCode) {
        try {
            Script script = Script.create(jsCode);

            taskExecutor.execute(script.getTaskToBeRun());

            scriptRepository.save(script);
            return script;
        } catch (TaskRejectedException e) {
            throw new CapacityViolationException("There is no free space in the pool");
        }
    }

    /**
     * Terminates the script by its id.
     *
     * @param id id of the running script.
     * @throws NoSuchScriptException if the script with a given id was not found.
     * @see ScriptRepository
     */
    public void stopById(Integer id) {
        findById(id).stop();
    }

    /**
     * Delete the script by its id, but only if it has one of the
     * next statuses: COMPLETED,FAILED,INTERRUPTED.
     *
     * @param id the id of the script
     * @throws IllegalStateException if the script did have appropriate status.
     * @see Status
     */
    public void delete(Integer id) {
        Script script = scriptRepository.findById(id);

        if (!Status.isFinished(script.getStatus()))
            throw new IllegalScriptStateException("Couldn't delete the script with id:%d due to its inappropriate state".formatted(id));

        scriptRepository.delete(id);
    }

}
