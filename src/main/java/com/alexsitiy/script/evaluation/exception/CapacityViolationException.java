package com.alexsitiy.script.evaluation.exception;

/**
 * This class extends {@link RuntimeException} says that
 * there is free place in the thread pool. It processed by {@link com.alexsitiy.script.evaluation.controller.ScriptControllerAdvice}
 */
public class CapacityViolationException extends RuntimeException {
    public CapacityViolationException(String message) {
        super(message);
    }
}
