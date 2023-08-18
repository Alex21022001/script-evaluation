package com.alexsitiy.script.evaluation.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class JSThreadPool implements ScriptThreadPool<JSScriptTask, Integer> {

    private static final Logger log = LoggerFactory.getLogger(JSThreadPool.class);

    private final ArrayBlockingQueue<JSScriptTask> tasks;
    private final List<ScriptThread<JSScriptTask>> threads = new CopyOnWriteArrayList<>();
    private final AtomicBoolean isRunning = new AtomicBoolean(true);

    public JSThreadPool(int nThreads, int queueCapacity) {
        this.tasks = new ArrayBlockingQueue<>(queueCapacity);
        for (int i = 0; i < nThreads; i++) {
            ScriptThread<JSScriptTask> thread = new ScriptThread<>(isRunning, tasks);
            thread.start();
            threads.add(thread);
        }
        log.debug("JSThreadPool is up");
    }

    @Override
    public void submit(JSScriptTask task) {
        tasks.add(task);
    }

    @Override
    public boolean stopTaskById(Integer id) {
        // TODO: 18.08.2023
        return false;
    }

    @Override
    public void shutdown() {
        isRunning.set(false);
        // TODO: 18.08.2023 Stop all executing tasks
    }
}
