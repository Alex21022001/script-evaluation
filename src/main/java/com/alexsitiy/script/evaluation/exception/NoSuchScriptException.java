package com.alexsitiy.script.evaluation.exception;

import com.alexsitiy.script.evaluation.controller.GlobalControllerAdvice;

/**
 * This class extends {@link RuntimeException} says that
 * there is no script with a given id. It processed by {@link GlobalControllerAdvice}
 */
public class NoSuchScriptException extends RuntimeException {
    private final Integer id;

    public NoSuchScriptException(String message, Integer id) {
        super(message);
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
}
