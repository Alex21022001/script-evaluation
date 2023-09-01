package com.alexsitiy.script.evaluation.model;

/**
 * The Enum with possible statuses for script's evaluation.
 */
public enum Status {
    IN_QUEUE,
    EXECUTING,
    COMPLETED,
    FAILED,
    INTERRUPTED;

    /**
     * Check whether a given status pertains to one of the finished statuses.
     * */
    public static boolean isFinished(Status status) {
        return status == INTERRUPTED || status == FAILED || status == COMPLETED;
    }
}
