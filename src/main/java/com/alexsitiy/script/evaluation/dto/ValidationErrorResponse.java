package com.alexsitiy.script.evaluation.dto;

import com.alexsitiy.script.evaluation.controller.GlobalControllerAdvice;
import jakarta.validation.ConstraintViolation;
import org.springframework.validation.BindingResult;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * This class is a representation of validation error that is used
 * to inform user of not passing validation.
 * <br/>
 * It's used by {@link GlobalControllerAdvice}
 */
@SuppressWarnings({"unused"})
public final class ValidationErrorResponse {

    private final int status;
    private final String error;
    private final Instant timestamp = Instant.now();
    private final List<Violation> violations;

    private ValidationErrorResponse(List<Violation> violations) {
        this.violations = violations;
        this.status = 400;
        this.error = "Bad Request";
    }

    public static ValidationErrorResponse of(Set<ConstraintViolation<?>> constraintViolations) {
        List<Violation> violations = new ArrayList<>();

        constraintViolations.forEach(constraintViolation -> violations.add(new Violation(
                constraintViolation.getMessage(),
                constraintViolation.getInvalidValue()
        )));

        return new ValidationErrorResponse(violations);
    }

    public static ValidationErrorResponse of(BindingResult bindingResult) {
        List<Violation> violations = new ArrayList<>();

        bindingResult.getFieldErrors().forEach(fieldError -> violations
                .add(new Violation(fieldError.getDefaultMessage(), fieldError.getRejectedValue())));

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
