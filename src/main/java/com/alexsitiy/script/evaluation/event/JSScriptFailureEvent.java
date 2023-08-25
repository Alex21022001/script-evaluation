package com.alexsitiy.script.evaluation.event;

import org.springframework.context.ApplicationEvent;

/**
 *  The class that extends {@link ApplicationEvent} and adds custom fields (execution time of the script and error).
 *  It's used as an event when {@link com.alexsitiy.script.evaluation.model.JSScript} is failed during its evaluation.
 *
 * @see com.alexsitiy.script.evaluation.listener.JSScriptFailureListener
 * @see com.alexsitiy.script.evaluation.thread.task.JSScriptTask
 * */
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
