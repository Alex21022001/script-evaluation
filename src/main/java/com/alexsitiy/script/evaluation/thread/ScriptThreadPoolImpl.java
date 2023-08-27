package com.alexsitiy.script.evaluation.thread;

import com.alexsitiy.script.evaluation.exception.CapacityViolationException;
import com.alexsitiy.script.evaluation.thread.task.ScriptTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 *  The implementation of {@linkplain ScriptThreadPool} that is used to run any {@linkplain ScriptTask}.
 *  Use {@linkplain ScriptThread} as a Thread for the pool. Use {@linkplain ArrayBlockingQueue} as a queue
 *  for holding {@linkplain ScriptTask} that will be executed by some generated threads.
 * */
public class ScriptThreadPoolImpl implements ScriptThreadPool {

    private static final Logger log = LoggerFactory.getLogger(ScriptThreadPoolImpl.class);

    private final ArrayBlockingQueue<ScriptTask> tasks;
    private final List<ScriptThread> threads = new ArrayList<>();
    private final AtomicBoolean isRunning = new AtomicBoolean(true);

    private final Integer queueCapacity;

    /**
     *  Create instance of {@linkplain ScriptThreadPoolImpl}.
     * @param nThreads the number of threads which will be added to the pool
     * @param queueCapacity the max number of possible tasks which can be in the queue
     * */
    public ScriptThreadPoolImpl(int nThreads, int queueCapacity) {
        this.queueCapacity = queueCapacity;
        this.tasks = new ArrayBlockingQueue<>(queueCapacity);

        for (int i = 0; i < nThreads; i++) {
            ScriptThread thread = new ScriptThread(isRunning, tasks);
            thread.start();
            threads.add(thread);
        }
        log.debug("JSThreadPool is up");
    }

    /**
     *  Add the task to the queue to run it latter.
     * @throws CapacityViolationException if there is no spare place in the queue.
     * */
    @Override
    public void submit(ScriptTask task) {
        if (tasks.size() == queueCapacity)
            throw new CapacityViolationException("Thread pool capacity exceeded. Please try again later.");

        tasks.add(task);
    }

    /**
     *  Stop the task by id, by looking through the queue and threads
     * */
    @Override
    public <T extends Number> boolean stopTaskById(T id) {
//        if (tasks.removeIf(task -> task.getScript().getId().equals(id))) {
//            log.debug("Task with id: {} was deleted from the Queue", id);
//            return true;
//        }

        Optional<ScriptThread> jsTask = threads.stream()
                .filter(thread -> thread.getCurrentTaskId() != null)
                .filter(thread -> thread.getCurrentTaskId().equals(id))
                .findFirst();

        return jsTask
                .map(ScriptThread::stopCurrentTask)
                .orElse(false);
    }


    @Override
    public void shutdown() {
        isRunning.set(false);
        log.debug("ScriptThreadPool is finished");
    }
}
