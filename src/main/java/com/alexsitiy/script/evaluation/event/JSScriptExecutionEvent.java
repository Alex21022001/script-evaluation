package com.alexsitiy.script.evaluation.event;

import com.alexsitiy.script.evaluation.model.JSScript;
import org.springframework.context.ApplicationEvent;

public class JSScriptExecutionEvent extends ApplicationEvent {

    public JSScriptExecutionEvent(Object source) {
        super(source);
    }
}
