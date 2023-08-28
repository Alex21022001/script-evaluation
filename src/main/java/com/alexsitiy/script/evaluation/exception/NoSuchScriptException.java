package com.alexsitiy.script.evaluation.exception;

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
