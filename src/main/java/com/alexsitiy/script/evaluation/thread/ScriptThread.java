package com.alexsitiy.script.evaluation.thread;

import com.alexsitiy.script.evaluation.thread.task.ScriptTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class ScriptThread extends Thread {

    protected static final Logger log = LoggerFactory.getLogger(ScriptThread.class);

    private final AtomicBoolean isRunning;
    private final BlockingQueue<ScriptTask> tasks;

    private final AtomicReference<ScriptTask> currentTask = new AtomicReference<>();

    protected ScriptThread(AtomicBoolean isRunning, BlockingQueue<ScriptTask> tasks) {
        this.isRunning = isRunning;
        this.tasks = tasks;
    }

    public boolean stopCurrentTask() {
        if (getCurrentTask().get() == null) {
            log.debug("Couldn't stop current task in {}, because current Task = null", this.getName());
            return false;
        }

        ScriptTask task = getCurrentTask().get();
        return task.stop();
    }

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

    public AtomicReference<ScriptTask> getCurrentTask() {
        return currentTask;
    }
}

