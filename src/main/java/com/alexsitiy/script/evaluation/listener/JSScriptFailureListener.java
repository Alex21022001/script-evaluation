package com.alexsitiy.script.evaluation.listener;

import com.alexsitiy.script.evaluation.event.JSScriptCompletionEvent;
import com.alexsitiy.script.evaluation.event.JSScriptFailureEvent;
import com.alexsitiy.script.evaluation.model.JSScript;
import com.alexsitiy.script.evaluation.model.Status;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class JSScriptFailureListener {

    @EventListener
    public void onJSScriptExecution(JSScriptFailureEvent event){
        JSScript jsScript = (JSScript) event.getSource();
        jsScript.setStatus(Status.FAILED); // Here could be invoked JSScriptService/Repository
        jsScript.setExecutionTime(event.getExecutionTime());
        jsScript.setErrors(event.getError());
    }
}
