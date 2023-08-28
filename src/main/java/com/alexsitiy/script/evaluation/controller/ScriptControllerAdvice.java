package com.alexsitiy.script.evaluation.controller;

import com.alexsitiy.script.evaluation.exception.NoSuchScriptException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
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
    public ProblemDetail handleNoSuchScriptException(IllegalStateException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
        problemDetail.setTitle("Script is not finished yet");

        return problemDetail;
    }
}
