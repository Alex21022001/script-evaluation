package com.alexsitiy.script.evaluation.exception;

/**
 * This exception says that a given user's data are invalid.
 */
public class InvalidUserDataException extends RuntimeException {
    public InvalidUserDataException(String message) {
        super(message);
    }
}
