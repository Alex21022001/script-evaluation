package com.alexsitiy.script.evaluation.listener;

import com.alexsitiy.script.evaluation.event.JSScriptExecutionEvent;
import com.alexsitiy.script.evaluation.model.JSScript;
import com.alexsitiy.script.evaluation.model.Status;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * The listener that listens to {@link JSScriptExecutionEvent}.
 * Change the {@link Status} of {@link JSScript} to EXECUTING.
 *
 * @see com.alexsitiy.script.evaluation.thread.task.ScriptTask
 * @see Component
 * @see EventListener
 */
@Component
public class JSScriptExecutionListener {

    /**
     * Changes the status of the script obtained from event object
     * to EXECUTING.
     *
     * @param event the occurred event.
     * @see JSScript
     * @see Status
     */
    @EventListener
    public void onJSScriptExecution(JSScriptExecutionEvent event) {
        JSScript jsScript = (JSScript) event.getSource();
        jsScript.setStatus(Status.EXECUTING); // Here could be invoked JSScriptService/Repository
    }
}
