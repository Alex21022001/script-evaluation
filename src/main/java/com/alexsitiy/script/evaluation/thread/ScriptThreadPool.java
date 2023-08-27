package com.alexsitiy.script.evaluation.thread;

import com.alexsitiy.script.evaluation.thread.task.ScriptTask;

/**
 * This interface provides a way of submission {@linkplain ScriptTask}
 * It's normally used instead of explicitly creating threads. It also adds additional
 * control over executing tasks as stopping the task by its id.
 *
 * @author Alex Sitiy
 * @see ScriptThread
 * */
public interface ScriptThreadPool {

    /**
     * Add {@linkplain ScriptTask} to the queue to execute it in the future when
     * there will be any spare Thread.
     * */
    void submit(ScriptTask task);

    /**
     * Stop the running task or delete it from the queue.
     * @return true - if the task was found and stopped/deleted,
     * false - if there was no task in the queue or didn't manage to stop the task.
     * */
    <T extends Number> boolean stopTaskById(T id);

    /**
     * Terminate all Threads in the pool.
     * */
    void shutdown();
}
