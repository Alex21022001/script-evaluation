package com.alexsitiy.script.evaluation.event;

import org.springframework.context.ApplicationEvent;

public class JSScriptFailureEvent extends ApplicationEvent {

    private final long executionTime;
    private final String error;

    public String getError() {
        return error;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public JSScriptFailureEvent(Object source, long executionTime, String error) {
        super(source);
        this.executionTime = executionTime;
        this.error = error;
    }
}
