package com.alexsitiy.script.evaluation.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class JSThreadPool implements ScriptThreadPool<JSScriptTask, Integer> {

    private static final Logger log = LoggerFactory.getLogger(JSThreadPool.class);

    private final ArrayBlockingQueue<JSScriptTask> tasks;
    private final List<ScriptThread<JSScriptTask>> threads = new ArrayList<>();
    private final AtomicBoolean isRunning = new AtomicBoolean(true);

    public JSThreadPool(int nThreads, int queueCapacity) {
        this.tasks = new ArrayBlockingQueue<>(queueCapacity);
        for (int i = 0; i < nThreads; i++) {
            ScriptThread<JSScriptTask> thread = new JSScriptThread(isRunning, tasks);
            thread.start();
            threads.add(thread);
        }
        log.debug("JSThreadPool is up");
    }

    @Override
    public void submit(JSScriptTask task) {
        // TODO: 18.08.2023 Add check if there is free place in queue.
        tasks.add(task);
    }

    @Override
    public boolean stopTaskById(Integer id) {
        if (tasks.removeIf(jsScriptTask -> jsScriptTask.getJsScript().getId().equals(id))) {
            log.debug("Task with id: {} was deleted from the Queue", id);
            return true;
        }

        Optional<ScriptThread<JSScriptTask>> jsTask = threads.stream()
                .filter(thread -> thread.getCurrentTask().get() != null)
                .filter(thread -> thread.getCurrentTask().get().getJsScript().getId().equals(id))
                .findFirst();


        return jsTask
                .map(ScriptThread::stopCurrentTask)
                .orElse(false);
    }

    @Override
    public void shutdown() {
        isRunning.set(false);
        // TODO: 18.08.2023 Stop all executing tasks
    }
}
