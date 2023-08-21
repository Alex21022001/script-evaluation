package com.alexsitiy.script.evaluation.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public abstract class ScriptThread<T extends Runnable> extends Thread {

    protected static final Logger log = LoggerFactory.getLogger(ScriptThread.class);

    private final AtomicBoolean isRunning;
    private final BlockingQueue<T> tasks;

    private final AtomicReference<T> currentTask = new AtomicReference<>();

    protected ScriptThread(AtomicBoolean isRunning, BlockingQueue<T> tasks) {
        this.isRunning = isRunning;
        this.tasks = tasks;
    }

    public abstract boolean stopCurrentTask();

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

    public AtomicReference<T> getCurrentTask() {
        return currentTask;
    }
}

