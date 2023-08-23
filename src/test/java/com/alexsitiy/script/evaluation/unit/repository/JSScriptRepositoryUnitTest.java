package com.alexsitiy.script.evaluation.unit.repository;

import com.alexsitiy.script.evaluation.model.JSScript;
import com.alexsitiy.script.evaluation.model.Status;
import com.alexsitiy.script.evaluation.repository.JSScriptRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.alexsitiy.script.evaluation.util.JSScriptUtils.getValidJSCode;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class JSScriptRepositoryUnitTest {

    private static final Integer JS_SCRIPT_ID = 0;

    @InjectMocks
    private JSScriptRepository jsScriptRepository;


    @Test
    void findById() {
        jsScriptRepository.create(getValidJSCode());

        Optional<JSScript> actual = jsScriptRepository.findById(JS_SCRIPT_ID);

        assertThat(actual).isPresent()
                .get()
                .hasFieldOrPropertyWithValue("body", getValidJSCode())
                .hasFieldOrPropertyWithValue("id", JS_SCRIPT_ID);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    void findByInvalidId() {
        Optional<JSScript> actual = jsScriptRepository.findById(JS_SCRIPT_ID);

        assertThat(actual).isEmpty();
    }

    @Test
    void create() {
        JSScript actual = jsScriptRepository.create(getValidJSCode());

        assertThat(actual).isNotNull()
                .hasFieldOrPropertyWithValue("id", 0)
                .hasFieldOrPropertyWithValue("body", getValidJSCode())
                .hasFieldOrPropertyWithValue("status", Status.IN_QUEUE);
    }

    @Test
    void delete() {
        jsScriptRepository.create(getValidJSCode());

        boolean actual = jsScriptRepository.delete(JS_SCRIPT_ID);

        assertThat(actual).isTrue();
    }

    @Test
    void deleteByIdAndStatus() {
        jsScriptRepository.create(getValidJSCode());
        jsScriptRepository.findById(JS_SCRIPT_ID).get().setStatus(Status.COMPLETED);

        Assertions.assertTrue(jsScriptRepository.delete(JS_SCRIPT_ID, List.of(Status.COMPLETED)));
    }


    @ParameterizedTest
    @ValueSource(ints = {0, 10, 1})
    void deleteByInvalidId() {
        boolean actual = jsScriptRepository.delete(JS_SCRIPT_ID);

        assertThat(actual).isFalse();
    }
}