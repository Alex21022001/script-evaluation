package com.alexsitiy.script.evaluation.event;

import org.springframework.context.ApplicationEvent;

public class JSScriptCompletionEvent extends ApplicationEvent {

    private final long executionTime;

    public long getExecutionTime() {
        return executionTime;
    }

    public JSScriptCompletionEvent(Object source, long executionTime) {
        super(source);
        this.executionTime = executionTime;
    }
}
