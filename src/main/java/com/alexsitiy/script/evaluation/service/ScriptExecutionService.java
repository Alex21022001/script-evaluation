package com.alexsitiy.script.evaluation.service;

/**
 *  The abstraction for evaluating and stopping scripts.
 * */
public interface ScriptExecutionService<R, P> {
    /**
     * The abstract method that evaluates script {@link P} and return the representation of it {@link R}
     *
     * @param value the script that will be evaluated.
     * @return R - the representation of an evaluating script.
     * */
    R evaluate(P value);

    /**
     *  The abstract method for stopping the running script by its id.
     *
     * @param id the id of the running script.
     * @return true - if the script was found and terminated, false - not.
     * */
    <T extends Number> boolean stopById(T id);
}
