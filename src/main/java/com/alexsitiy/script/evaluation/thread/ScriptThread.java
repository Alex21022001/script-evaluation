package com.alexsitiy.script.evaluation.thread;

import com.alexsitiy.script.evaluation.thread.task.ScriptTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * The implementation of {@linkplain Thread} that is used in {@linkplain ScriptThreadPool}.
 *
 * */
public class ScriptThread extends Thread {

    protected static final Logger log = LoggerFactory.getLogger(ScriptThread.class);

    private final AtomicBoolean isRunning;
    private final BlockingQueue<ScriptTask> tasks;

    private final AtomicReference<ScriptTask> currentTask = new AtomicReference<>();

    protected ScriptThread(AtomicBoolean isRunning, BlockingQueue<ScriptTask> tasks) {
        this.isRunning = isRunning;
        this.tasks = tasks;
    }

    /**
     *  Stop the task that is currently running in the Thread by using ScriptTask.stop().
     *
     * @return true - if the task was terminated, false - if not.
     * @see ScriptTask
     * */
    public boolean stopCurrentTask() {
        if (this.currentTask.get() == null) {
            log.debug("Couldn't stop current task in {}, because current Task = null", this.getName());
            return false;
        }

        ScriptTask task = this.currentTask.get();
        return task.stop();
    }

    /**
     *  Checking an available task in the task pool (queue) and run if any exists.
     *  Before running the task adds it to currentTask field to track the currently running task.
     *  After the task is finished - removes it from currentTask field.
     *
     * @throws InterruptedException if the thread was forcibly terminated during its waiting for a task.
     * */
    @Override
    public void run() {
        while (isRunning.get()) {
            try {
                var nextTask = tasks.take();
                currentTask.set(nextTask);
                log.debug("Current task in Thread {} was changed to {}", this.getName(), currentTask.get());

                nextTask.run();

                currentTask.set(null);
                log.debug("Thread {} finished its Task", this.getName());
            } catch (InterruptedException e) {
                log.debug("Thread {} is interrupted", this.getName());
            }
        }
    }

    /**
     *  Get the id of the task that is currently running
     *
     * @return Any implementation of Number that is the id of the running task
     * */
    public Number getCurrentTaskId() {
        return currentTask.get() != null ? 1 : null;
    }
}

