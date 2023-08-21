package com.alexsitiy.script.evaluation.thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class JSScriptThread extends ScriptThread<JSScriptTask> {

    protected JSScriptThread(AtomicBoolean isRunning, BlockingQueue<JSScriptTask> tasks) {
        super(isRunning, tasks);
    }

    @Override
    public boolean stopCurrentTask() {
        if (getCurrentTask().get() == null) {
            log.debug("Couldn't stop current task in {}, because current Task = null", this.getName());
            return false;
        }

        JSScriptTask jsScriptTask = getCurrentTask().get();
        return jsScriptTask.interruptContext();
    }
}
