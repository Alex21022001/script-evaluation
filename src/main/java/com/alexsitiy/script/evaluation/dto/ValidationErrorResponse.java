package com.alexsitiy.script.evaluation.dto;

import jakarta.validation.ConstraintViolation;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public final class ValidationErrorResponse {

    private final int status = 400;
    private final String error = "Bad Request";
    private final Instant timestamp = Instant.now();
    private final List<Violation> violations;


    private ValidationErrorResponse(List<Violation> violations) {
        this.violations = violations;
    }

    public static ValidationErrorResponse of(Set<ConstraintViolation<?>> constraintViolations) {
        List<Violation> violations = new ArrayList<>();

        constraintViolations.forEach(constraintViolation -> {
            violations.add(new Violation(
                    constraintViolation.getMessage(),
                    constraintViolation.getInvalidValue()
            ));
        });

        return new ValidationErrorResponse(violations);
    }

    public String getError() {
        return error;
    }

    public int getStatus() {
        return status;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public List<Violation> getViolations() {
        return violations;
    }

    record Violation(
            String message,
            Object rejectedValue
    ) {

    }
}
