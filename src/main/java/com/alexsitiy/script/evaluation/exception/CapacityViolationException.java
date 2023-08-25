package com.alexsitiy.script.evaluation.exception;

/**
 * This exception says that the capacity of ThreadPool,task queue and so on
 * was exceeded.
 *
 * @see com.alexsitiy.script.evaluation.thread.ScriptThreadPool
 */
public class CapacityViolationException extends RuntimeException {
    public CapacityViolationException(String message) {
        super(message);
    }
}
