package com.alexsitiy.script.evaluation.event;

import org.springframework.context.ApplicationEvent;

/**
 *  The class that extends {@link ApplicationEvent}.
 *  It's used as an event when {@link com.alexsitiy.script.evaluation.model.JSScript} is just started to being evaluated.
 *
 * @see com.alexsitiy.script.evaluation.listener.JSScriptExecutionListener
 * @see com.alexsitiy.script.evaluation.thread.task.JSScriptTask
 * */
public class JSScriptExecutionEvent extends ApplicationEvent {

    public JSScriptExecutionEvent(Object source) {
        super(source);
    }
}
