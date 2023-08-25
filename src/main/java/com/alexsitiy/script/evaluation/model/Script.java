package com.alexsitiy.script.evaluation.model;

/**
 * The main interface that is used to create script which holds all the necessary information
 * for its evaluation. ID field is mandatory.
 *
 * @author Alex Sitiy
 * @see com.alexsitiy.script.evaluation.thread.task.ScriptTask
 * @see #getId()
 * */
public interface Script {

    /**
     * @return  Script's id.
     * */
    Number getId();
}
