package com.alexsitiy.script.evaluation.listener;

import com.alexsitiy.script.evaluation.event.JSScriptInterruptedEvent;
import com.alexsitiy.script.evaluation.model.JSScript;
import com.alexsitiy.script.evaluation.model.Status;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class JSScriptInterruptedListener {

    @EventListener
    public void onJSScriptExecution(JSScriptInterruptedEvent event){
        JSScript jsScript = (JSScript) event.getSource();
        jsScript.setStatus(Status.INTERRUPTED); // Here could be invoked JSScriptService/Repository
        jsScript.setExecutionTime(event.getExecutionTime());
        jsScript.setErrors(event.getError());
    }
}
