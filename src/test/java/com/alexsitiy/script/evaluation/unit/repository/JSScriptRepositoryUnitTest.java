package com.alexsitiy.script.evaluation.unit.repository;

import com.alexsitiy.script.evaluation.model.JSScript;
import com.alexsitiy.script.evaluation.model.Status;
import com.alexsitiy.script.evaluation.repository.JSScriptRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class JSScriptRepositoryUnitTest {

    private static final String JS_CODE = "console.log()";
    private static final Integer JS_SCRIPT_ID = 0;

    @InjectMocks
    private JSScriptRepository jsScriptRepository;


    @Test
    void findAll() {
        // TODO: 20.08.2023
    }

    @Test
    void findById() {
        jsScriptRepository.create(JS_CODE);

        Optional<JSScript> actual = jsScriptRepository.findById(JS_SCRIPT_ID);

        assertThat(actual).isPresent()
                .get()
                .hasFieldOrPropertyWithValue("body", JS_CODE)
                .hasFieldOrPropertyWithValue("id", JS_SCRIPT_ID);
    }

    @ParameterizedTest
    @ValueSource(ints = {0,1})
    void findByInvalidId() {
        Optional<JSScript> actual = jsScriptRepository.findById(JS_SCRIPT_ID);

        assertThat(actual).isEmpty();
    }

    @Test
    void create() {
        JSScript actual = jsScriptRepository.create(JS_CODE);

        assertThat(actual).isNotNull()
                .hasFieldOrPropertyWithValue("id", 0)
                .hasFieldOrPropertyWithValue("body", JS_CODE)
                .hasFieldOrPropertyWithValue("status", Status.IN_QUEUE);
    }

    @Test
    void delete() {
        jsScriptRepository.create(JS_CODE);

        boolean actual = jsScriptRepository.delete(JS_SCRIPT_ID);

        assertThat(actual).isTrue();
    }

    @ParameterizedTest
    @ValueSource(ints = {0,10,1})
    void deleteByInvalidId() {
        boolean actual = jsScriptRepository.delete(JS_SCRIPT_ID);

        assertThat(actual).isFalse();
    }
}