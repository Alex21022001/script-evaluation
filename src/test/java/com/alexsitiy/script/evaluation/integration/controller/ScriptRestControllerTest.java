package com.alexsitiy.script.evaluation.integration.controller;

import com.alexsitiy.script.evaluation.integration.IntegrationTestBase;
import com.alexsitiy.script.evaluation.model.Status;
import com.alexsitiy.script.evaluation.service.ScriptExecutionService;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static com.alexsitiy.script.evaluation.util.JSScriptUtils.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ScriptRestControllerTest extends IntegrationTestBase {

    private static final String BASE_URI = "/js/scripts";
    private static final Integer NEXT_SCRIPT_ID = 6;

    private final MockMvc mockMvc;
    private final ScriptExecutionService jsService;

    @Autowired
    ScriptRestControllerTest(MockMvc mockMvc, ScriptExecutionService jsService) {
        this.mockMvc = mockMvc;
        this.jsService = jsService;
    }

    @BeforeAll
    void initInitialScripts() {
        jsService.evaluate(getValidJSCode());
        jsService.evaluate(getValidJSCode());
        jsService.evaluate(getValidJSCode());
        jsService.evaluate(getInvalidJsCode());
        jsService.evaluate(getInvalidJsCode());
        jsService.evaluate(getEndlessJsCode());
        Awaitility
                .await()
                .pollDelay(Duration.of(1, ChronoUnit.SECONDS))
                .untilAsserted(() -> Assertions.assertTrue(true));
    }

    @Test
    @Order(1)
    void findAll() throws Exception {
        mockMvc.perform(get(BASE_URI)
                        .queryParam("statuses", "COMPLETED,EXECUTING")
                        .queryParam("sort", "ID")
                )
                .andExpect(status().is2xxSuccessful())
                .andExpectAll(
                        jsonPath("_embedded.jSScriptReadDtoList.size()").value(4)
                );
    }

    @Test
    @Order(2)
    void findById() throws Exception {
        int id = 0;
        mockMvc.perform(get(BASE_URI + "/{id}", id))
                .andExpect(status().is2xxSuccessful())
                .andExpectAll(
                        jsonPath("id").value(id),
                        jsonPath("status").value(Status.COMPLETED.name()),
                        jsonPath("body").value(getValidJSCode()),
                        jsonPath("result").exists(),
                        jsonPath("errors", is(nullValue()))
                );
    }

    @Test
    @Order(3)
    void evaluate() throws Exception {
        mockMvc.perform(post(BASE_URI + "/evaluate")
                        .content(getValidJSCode()))
                .andExpect(status().is(201))
                .andExpectAll(
                        jsonPath("id").value(NEXT_SCRIPT_ID),
                        jsonPath("status").value(Status.IN_QUEUE.name()),
                        jsonPath("body").value(getValidJSCode()),
                        jsonPath("result").exists(),
                        jsonPath("errors", is(nullValue()))
                );
    }

    @Test
    @Order(4)
    void stop() throws Exception {
        int id = 5;
        mockMvc.perform(post(BASE_URI + "/stop/{id}", id))
                .andExpect(status().is(204));
    }

    @Test
    @Order(5)
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URI+"/{id}",0))
                .andExpect(status().is(204));
    }
}