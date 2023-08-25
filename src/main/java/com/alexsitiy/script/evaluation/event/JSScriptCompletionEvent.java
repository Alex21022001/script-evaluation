package com.alexsitiy.script.evaluation.event;

import org.springframework.context.ApplicationEvent;


/**
 *  The class that extends {@link ApplicationEvent} and adds custom field (execution time of the script).
 *  It's used as an event when {@link com.alexsitiy.script.evaluation.model.JSScript} is successfully completed.
 *
 * @see com.alexsitiy.script.evaluation.listener.JSScriptCompletionListener
 * @see com.alexsitiy.script.evaluation.thread.task.JSScriptTask
 * */
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
