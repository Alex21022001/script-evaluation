package com.alexsitiy.script.evaluation.controller;

import com.alexsitiy.script.evaluation.dto.ValidationErrorResponse;
import com.alexsitiy.script.evaluation.exception.CapacityViolationException;
import com.alexsitiy.script.evaluation.exception.NoSuchScriptException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ScriptControllerAdvice {

    @ExceptionHandler(NoSuchScriptException.class)
    public ProblemDetail handleNoSuchScriptException(NoSuchScriptException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        problemDetail.setTitle("Script Not Found");
        problemDetail.setProperty("scriptId", e.getId());

        return problemDetail;
    }

    @ExceptionHandler(IllegalStateException.class)
    public ProblemDetail handleIllegalStateException(IllegalStateException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.METHOD_NOT_ALLOWED, e.getMessage());
        problemDetail.setTitle("Script is not finished yet");

        return problemDetail;
    }

    @ExceptionHandler(CapacityViolationException.class)
    public ProblemDetail handleCapacityViolationException(CapacityViolationException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.TOO_MANY_REQUESTS, e.getMessage());
        problemDetail.setTitle("To many request, try latter");

        return problemDetail;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ValidationErrorResponse> handleConstraintViolationException(ConstraintViolationException e){
       return ResponseEntity
               .badRequest()
               .body(ValidationErrorResponse.of(e.getConstraintViolations()));
    }
}
