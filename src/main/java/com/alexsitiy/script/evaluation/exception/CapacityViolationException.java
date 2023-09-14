package com.alexsitiy.script.evaluation.exception;

import com.alexsitiy.script.evaluation.controller.GlobalControllerAdvice;

/**
 * This class extends {@link RuntimeException} says that
 * there is free place in the thread pool. It processed by {@link GlobalControllerAdvice}
 */
public class CapacityViolationException extends RuntimeException {
    public CapacityViolationException(String message) {
        super(message);
    }
}
