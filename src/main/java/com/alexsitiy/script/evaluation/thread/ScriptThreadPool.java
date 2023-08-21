package com.alexsitiy.script.evaluation.thread;

import com.alexsitiy.script.evaluation.thread.task.ScriptTask;

public interface ScriptThreadPool {

    void submit(ScriptTask task);

    <T extends Number> boolean stopTaskById(T id);

    void shutdown();
}
