package com.alexsitiy.script.evaluation.service;

import com.alexsitiy.script.evaluation.dto.JSScriptReadDto;
import com.alexsitiy.script.evaluation.model.JSScript;
import com.alexsitiy.script.evaluation.model.Status;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class JSScriptService {

    private AtomicInteger idCounter = new AtomicInteger();

    public JSScript create(String jsCode) {
        // TODO: 18.08.2023 create inner List to save Scripts
        return new JSScript(
                idCounter.incrementAndGet(),
                Status.IN_QUEUE,
                null,
                jsCode,
                new ByteArrayOutputStream(),
                new ByteArrayOutputStream()
        );
    }
}
