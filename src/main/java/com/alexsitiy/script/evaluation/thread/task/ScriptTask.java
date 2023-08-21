package com.alexsitiy.script.evaluation.thread.task;

import com.alexsitiy.script.evaluation.model.Script;

public interface ScriptTask extends Runnable {

    boolean stop();
    Script getScript();
}
