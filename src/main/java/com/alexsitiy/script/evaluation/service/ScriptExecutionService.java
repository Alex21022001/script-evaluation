package com.alexsitiy.script.evaluation.service;

public interface ScriptExecutionService<R, P> {
    R evaluate(P value);

    <T extends Number> boolean stopById(T id);
}
