package com.alexsitiy.script.evaluation.model;

/**
 * The Enum with possible statuses for script's evaluation.
 *
 * @see JSScript
 */
public enum Status {
    IN_QUEUE,
    EXECUTING,
    COMPLETED,
    FAILED,
    INTERRUPTED
}
