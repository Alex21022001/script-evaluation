package com.alexsitiy.script.evaluation.exception;

/**
 * This class extends {@link RuntimeException} says that
 * there is no script with a given id. It processed by {@link com.alexsitiy.script.evaluation.controller.ScriptControllerAdvice}
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
