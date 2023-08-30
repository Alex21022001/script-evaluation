package com.alexsitiy.script.evaluation.dto;

import com.alexsitiy.script.evaluation.model.Status;

import java.time.Instant;

public record ScriptReadDto(
        Integer id,
        Status status,
        Long executionTime,
        Instant scheduledTime,
        String body,
        String result
) {

}
