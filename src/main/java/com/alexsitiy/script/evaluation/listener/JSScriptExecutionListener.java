package com.alexsitiy.script.evaluation.listener;

import com.alexsitiy.script.evaluation.event.JSScriptExecutionEvent;
import com.alexsitiy.script.evaluation.model.JSScript;
import com.alexsitiy.script.evaluation.model.Status;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class JSScriptExecutionListener {

    @EventListener
    public void onJSScriptExecution(JSScriptExecutionEvent event){
        JSScript jsScript = (JSScript) event.getSource();
        jsScript.setStatus(Status.EXECUTING); // Here could be invoked JSScriptService/Repository
    }
}
