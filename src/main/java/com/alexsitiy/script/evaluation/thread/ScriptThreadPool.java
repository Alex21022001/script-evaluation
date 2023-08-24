package com.alexsitiy.script.evaluation.thread;

import com.alexsitiy.script.evaluation.thread.task.ScriptTask;

/**
 * This interface provides a way of submission {@linkplain ScriptTask}
 * It's normally used instead of explicitly creating threads. It also adds additional
 * control over executing tasks as stopping the task by its id.
 *
 * @author Alex Sitiy
 * @see com.alexsitiy.script.evaluation.model.Script
 * @see ScriptThread
 * */
public interface ScriptThreadPool {

    void submit(ScriptTask task);

    <T extends Number> boolean stopTaskById(T id);

    void shutdown();
}
