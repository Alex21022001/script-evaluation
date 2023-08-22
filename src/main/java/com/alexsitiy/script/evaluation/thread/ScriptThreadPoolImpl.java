package com.alexsitiy.script.evaluation.thread;

import com.alexsitiy.script.evaluation.thread.task.ScriptTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class ScriptThreadPoolImpl implements ScriptThreadPool {

    private static final Logger log = LoggerFactory.getLogger(ScriptThreadPoolImpl.class);

    private final ArrayBlockingQueue<ScriptTask> tasks;
    private final List<ScriptThread> threads = new ArrayList<>();
    private final AtomicBoolean isRunning = new AtomicBoolean(true);

    public ScriptThreadPoolImpl(int nThreads, int queueCapacity) {
        this.tasks = new ArrayBlockingQueue<>(queueCapacity);

        for (int i = 0; i < nThreads; i++) {
            ScriptThread thread = new ScriptThread(isRunning, tasks);
            thread.start();
            threads.add(thread);
        }
        log.debug("JSThreadPool is up");
    }

    @Override
    public void submit(ScriptTask task) {
        // TODO: 18.08.2023 Add check if there is free place in queue.
        tasks.add(task);
    }

    @Override
    public <T extends Number> boolean stopTaskById(T id) {
        if (tasks.removeIf(task -> task.getScript().getId().equals(id))) {
            log.debug("Task with id: {} was deleted from the Queue", id);
            return true;
        }

        Optional<ScriptThread> jsTask = threads.stream()
                .filter(thread -> thread.getCurrentTask().get() != null)
                .filter(thread -> thread.getCurrentTask().get().getScript().getId().equals(id))
                .findFirst();

        return jsTask
                .map(ScriptThread::stopCurrentTask)
                .orElse(false);
    }


    @Override
    public void shutdown() {
        log.debug("ScriptThreadPool is finished");
        isRunning.set(false);
    }
}
