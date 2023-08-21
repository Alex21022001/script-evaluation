package com.alexsitiy.script.evaluation.event;

import org.springframework.context.ApplicationEvent;

public class JSScriptInterruptedEvent extends ApplicationEvent {

    private final long executionTime;
    private final String error;

    public String getError() {
        return error;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public JSScriptInterruptedEvent(Object source, long executionTime, String error) {
        super(source);
        this.executionTime = executionTime;
        this.error = error;
    }
}
