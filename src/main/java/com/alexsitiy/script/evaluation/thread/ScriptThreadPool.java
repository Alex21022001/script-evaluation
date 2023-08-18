package com.alexsitiy.script.evaluation.thread;

public interface ScriptThreadPool<T extends Runnable,I> {

    void submit(T task);
    boolean stopTaskById(I id);
    void shutdown();
}
