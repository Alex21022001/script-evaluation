package com.alexsitiy.script.evaluation.controller;

import com.alexsitiy.script.evaluation.dto.ValidationErrorResponse;
import com.alexsitiy.script.evaluation.exception.*;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * This class is used for handling exceptions which can occur during processing
 * user's request and return an appropriate response with mediaType: application/problem+json.
 *
 * @see MediaType
 */
@ControllerAdvice
public class GlobalControllerAdvice {

    /**
     * Handles {@linkplain NoSuchScriptException} that can occur when
     * no script was found by a specified id. Returns 404(NOT_FOUND)
     *
     * @param e NoSuchScriptException that need to be solved.
     * @return {@linkplain ProblemDetail} - representation of the response with 404 status code.
     */
    @ExceptionHandler(NoSuchScriptException.class)
    public ProblemDetail handleNoSuchScriptException(NoSuchScriptException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        problemDetail.setTitle("Script Not Found");
        problemDetail.setProperty("scriptId", e.getId());

        return problemDetail;
    }

    /**
     * Handles {@linkplain IllegalStateException} that can occur when
     * user trys to delete script that has not finished yet. Returns 405(METHOD_NOT_ALLOWED).
     *
     * @param e IllegalStateException that need to be solved.
     * @return {@linkplain ProblemDetail} - representation of the response with 405 status code.
     */
    @ExceptionHandler(IllegalScriptStateException.class)
    public ProblemDetail handleIllegalScriptStateException(IllegalScriptStateException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.METHOD_NOT_ALLOWED, e.getMessage());
        problemDetail.setTitle("Script is not finished yet");

        return problemDetail;
    }

    /**
     * Handles {@linkplain CapacityViolationException} that can occur when
     * user makes too many request to evaluating scripts. Returns 429(TOO-MANY_REQUESTS).
     *
     * @param e CapacityViolationException that need to be solved.
     * @return {@linkplain ProblemDetail} - representation of the response with 429 status code.
     */
    @ExceptionHandler(CapacityViolationException.class)
    public ProblemDetail handleCapacityViolationException(CapacityViolationException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.TOO_MANY_REQUESTS, e.getMessage());
    }


    @ExceptionHandler(ScriptNotValidException.class)
    public ProblemDetail handleScriptNotValidException(ScriptNotValidException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(InvalidUserDataException.class)
    public ProblemDetail handleInvalidUserDataException(InvalidUserDataException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResponseEntity
                .badRequest()
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(ValidationErrorResponse.of(e.getBindingResult()));
    }

    /**
     * Handles {@linkplain ConstraintViolationException} that can occur when
     * user's request didn't pass the validation. Returns 400(BAD_REQUEST).
     *
     * @param e ConstraintViolationException that need to be solved.
     * @return {@linkplain ValidationErrorResponse} - representation of the response with 400 status code.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ValidationErrorResponse> handleConstraintViolationException(ConstraintViolationException e) {
        return ResponseEntity
                .badRequest()
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(ValidationErrorResponse.of(e.getConstraintViolations()));
    }
}
