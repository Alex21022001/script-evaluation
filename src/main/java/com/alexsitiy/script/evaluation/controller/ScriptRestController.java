package com.alexsitiy.script.evaluation.controller;

import com.alexsitiy.script.evaluation.dto.ScriptReadDto;
import com.alexsitiy.script.evaluation.mapper.ScriptReadMapper;
import com.alexsitiy.script.evaluation.model.JSScriptFilter;
import com.alexsitiy.script.evaluation.model.JSScriptSort;
import com.alexsitiy.script.evaluation.model.Script;
import com.alexsitiy.script.evaluation.service.ScriptExecutionService;
import com.alexsitiy.script.evaluation.service.ScriptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

///**
// * The main Rest controller that is responsible for obtaining user's
// * request which can include the following:
// * <br/>
// * 1. Evaluation of the JavaScript code.
// * <br/>
// * 2. Getting all scripts
// * <br/>
// * 3. Getting a specific script by its id.
// * <br/>
// * 4. Stopping a specific script.
// * <br/>
// * 5. Deleting already executed script by its id.
// * <br/>
// * This rest controller uses {@link org.springframework.hateoas.server.mvc.WebMvcLinkBuilder} to
// * build appropriate HATEOAS links in representations. It also includes Swagger descriptions
// * that is useful for testing the API.
// *
// * @see com.alexsitiy.script.evaluation.model.JSScript
// * @see JSScriptService
// * @see ScriptExecutionService
// * @see JSScriptReadDto
// * @see JSScriptFullReadDto
// * @see ResponseEntity
// */
@RestController
@RequestMapping("/js/scripts")
public class ScriptRestController {

    private final ScriptExecutionService scriptExecutionService;
    private final ScriptService scriptService;
    private final ScriptReadMapper scriptReadMapper;

    @Autowired
    public ScriptRestController(ScriptExecutionService scriptExecutionService,
                                ScriptService scriptService,
                                ScriptReadMapper scriptReadMapper) {
        this.scriptExecutionService = scriptExecutionService;
        this.scriptService = scriptService;
        this.scriptReadMapper = scriptReadMapper;
    }

    //    /**
//     * Finds all scripts which are storing in {@link com.alexsitiy.script.evaluation.repository.JSScriptRepository}
//     * according to passed {@link JSScriptFilter} and {@link JSScriptSort} objects.
//     * <br/>
//     * Sorting includes the following options:
//     * <br/>
//     * 1. id - sorts by script's id (asc)
//     * <br/>
//     * 2. ID - sorts by script;s id (desc)
//     * <br/>
//     * 3. time - sorts by script's execution time (asc)
//     * <br/>
//     * 4. TIME - sorts by script's execution time (desc)
//     * <br/>
//     * Filtering includes: COMPLETED,FAILED,INTERRUPTED,EXECUTING,IN_QUEUE.
//     * <p/>
//     * After getting the List of scripts adds HATEOAS link to each of them via {@code for-each}.
//     *
//     * @param filter an object that stores the information for filtering the script during the selection.
//     * @param sort   an object created via {@link com.alexsitiy.script.evaluation.config.JSScriptSortHandlerMethod} that us
//     *               used for sorting scripts.
//     * @return {@link ResponseEntity<CollectionModel>}(CollectionModel) that is used for HATEOAS representation,
//     * it comprises {@link JSScriptReadDto}. Also returns 200(OK) status code.
//     * @see HttpStatus
//     * @see JSScriptService
//     * @see com.alexsitiy.script.evaluation.model.Status
//     * @see org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
//     */
    @GetMapping
    @Operation(summary = "Obtains all available scripts, includes sorting and filtering",
            parameters = {
                    @Parameter(name = "filter", allowEmptyValue = true, example = "IN_QUEUE,COMPLETED,INTERRUPTED"),
                    @Parameter(name = "sort", allowEmptyValue = true, example = "TIME,id")
            })
    public ResponseEntity<CollectionModel<?>> findAll(JSScriptFilter filter, JSScriptSort sort) {

//        List<JSScriptReadDto> scripts = jsService.findAll(filter, sort);
//        scripts.forEach(jsScriptDto -> jsScriptDto
//                .add(linkTo(methodOn(JSScriptRestController.class).findById(jsScriptDto.getId())).withSelfRel()));
//
//        CollectionModel<JSScriptReadDto> collectionModel = CollectionModel.of(scripts,
//                linkTo(methodOn(JSScriptRestController.class).findAll(filter, sort)).withSelfRel());


        return null;
    }

    //    /**
//     * Finds {@linkplain com.alexsitiy.script.evaluation.model.JSScript} by its id. And returns
//     * its representation, also adds HATEOAS links to it.
//     *
//     * @param id script's id
//     * @return {@link ResponseEntity} that comprises {@link JSScriptFullReadDto},
//     * it also return 200(OK) or 404(NOT_FOUND) if there is no script with a requested id.
//     * @see JSScriptService
//     * @see JSScriptFullReadDto
//     * @see org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
//     * @see HttpStatus
//     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtains specif script by its id")
    public ResponseEntity<ScriptReadDto> findById(@PathVariable Integer id) {
        Script script = scriptService.findById(id);
        ScriptReadDto dto = scriptReadMapper.map(script);
        return ResponseEntity.ok(dto);
//        return jsService.findById(id)
//                .map(jsScriptDto -> {
//                    jsScriptDto
//                            .add(linkTo(methodOn(JSScriptRestController.class).findById(id)).withSelfRel())
//                            .add(linkTo(methodOn(JSScriptRestController.class).findAll(null, null)).withRel("allScripts").withType("GET"))
//                            .add(linkTo(methodOn(JSScriptRestController.class).stop(jsScriptDto.getId())).withRel("stop").withType("POST").withDeprecation("Stop an executing Script"))
//                            .add(linkTo(methodOn(JSScriptRestController.class).delete(jsScriptDto.getId())).withRel("delete").withType("DELETE").withDeprecation("Delete already executed Script"));
//                    return ResponseEntity.ok(jsScriptDto);
//                })
//                .orElseGet(ResponseEntity.notFound()::build);
    }

    /**
     * //     * Receives JavaScript as a String to evaluate it. After submitting the script
     * //     * to ThreadPool returns its representation that also includes HATEOAS links.
     * //     *
     * //     * @param jsCode JavaScript code that is evaluated.
     * //     * @return {@link ResponseEntity} that comprises afaf.
     * //     * It also returns 201(CREATED) status code.
     * //     * @see ScriptExecutionService
     * //     * @see com.alexsitiy.script.evaluation.thread.ScriptThreadPool
     * //     * @see org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
     * //     * @see HttpStatus
     * //
     */
    @PostMapping("/evaluate")
    @Operation(summary = "Evaluates passed script")
    public ResponseEntity<ScriptReadDto> evaluate(@RequestBody String jsCode) {
        // TODO: 28.08.2023 Validate script before adding to pool
        // TODO: 27.08.2023 Status 202 and return object with links
        Script script = scriptExecutionService.evaluate(jsCode);
        ScriptReadDto dto = scriptReadMapper.map(script);

        return ResponseEntity
                .status(202)
                .body(dto);
    }

    //    /**
//     * Terminates the script that is currently running by its id, returns 404(NOT_FOUND) status code
//     * if such a script was not found.
//     *
//     * @param id running script's id
//     * @return 204(NO_CONTENT) if script was stopped, 404(NOT_FOUND) - if the script was not found.
//     * @see com.alexsitiy.script.evaluation.model.JSScript
//     * @see ScriptExecutionService
//     * @see org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
//     * @see HttpStatus
//     */
    @PostMapping("/stop/{id}")
    @Operation(summary = "Terminates a specific script by its id. Returns 404(NOT_FOUND) if such a script was not found in the pool or queue")
    public ResponseEntity<?> stop(@PathVariable Integer id) {

        scriptExecutionService.stopById(id);
        return ResponseEntity.noContent().build();
    }

    //    /**
//     * Deletes the script that is stored in {@link com.alexsitiy.script.evaluation.repository.JSScriptRepository}
//     * by its id. Returns 404(NOT_FOUND) status code if such a script was not found.
//     *
//     * @param id script's id
//     * @return 204(NO_CONTENT) if script was deleted, 404(NOT_FOUND) - if the script was not found.
//     * @see com.alexsitiy.script.evaluation.model.JSScript
//     * @see ScriptExecutionService
//     * @see org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
//     * @see HttpStatus
//     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes a COMPLETED,INTERRUPTED,FAILED script by its id")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        scriptService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
