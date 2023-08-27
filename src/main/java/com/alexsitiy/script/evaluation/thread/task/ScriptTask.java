package com.alexsitiy.script.evaluation.thread.task;

/**
 * The ScriptTask interface should be implemented by any
 * class whose instances are intended to be executed by a {@link com.alexsitiy.script.evaluation.thread.ScriptThreadPool}.
 *
 * @author Alex Sitiy
 * @see Runnable
 * @see com.alexsitiy.script.evaluation.thread.ScriptThread
 * */
public interface ScriptTask extends Runnable {

    /**
     * Stop the running Task.
     * */
    boolean stop();

    /**
     * @return  {@linkplain} that is used as main object for executing.
     * */
    Object getScript();
}
