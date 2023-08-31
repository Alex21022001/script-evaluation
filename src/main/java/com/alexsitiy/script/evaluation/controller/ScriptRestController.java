package com.alexsitiy.script.evaluation.controller;

import com.alexsitiy.script.evaluation.dto.ScriptReadDto;
import com.alexsitiy.script.evaluation.mapper.ScriptReadMapper;
import com.alexsitiy.script.evaluation.model.Script;
import com.alexsitiy.script.evaluation.model.Status;
import com.alexsitiy.script.evaluation.service.ScriptExecutionService;
import com.alexsitiy.script.evaluation.service.ScriptService;
import com.alexsitiy.script.evaluation.validation.CheckScript;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.NonComposite;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.Set;

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
@RequestMapping("/scripts")
@ExposesResourceFor(ScriptReadDto.class)
@Validated
public class ScriptRestController {

    private final ScriptExecutionService scriptExecutionService;
    private final ScriptService scriptService;

    private final EntityLinks entityLinks;
    private final ScriptReadMapper scriptReadMapper;


    @Autowired
    public ScriptRestController(ScriptExecutionService scriptExecutionService,
                                ScriptService scriptService,
                                EntityLinks entityLinks,
                                ScriptReadMapper scriptReadMapper) {
        this.scriptExecutionService = scriptExecutionService;
        this.scriptService = scriptService;
        this.entityLinks = entityLinks;
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
    public ResponseEntity<CollectionModel<ScriptReadDto>> findAll(@NonComposite @RequestParam(value = "statuses", required = false) Set<Status> statuses,
                                                                  @NonComposite @RequestParam(value = "sorts", required = false) List<String> sorts) {

        return ResponseEntity.ok(scriptReadMapper.toCollectionModel(scriptService.findAll(statuses, sorts)));
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
    public ResponseEntity<ScriptReadDto> findById(@PathVariable Integer id,
                                                  WebRequest request) {
        Script script = scriptService.findById(id);

        if (request.checkNotModified(script.getLastModified()))
            return ResponseEntity.status(304).build();

        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.noCache().cachePrivate())
                .lastModified(script.getLastModified())
                .body(scriptReadMapper.toModelWithAllLinks(script));
    }

    @GetMapping(value = "/{id}/body", produces = {"text/plain"})
    public ResponseEntity<String> getBody(@PathVariable Integer id) {
        return ResponseEntity
                .ok()
                .header(HttpHeaders.LINK,
                        entityLinks.linkForItemResource(ScriptReadDto.class, id).withSelfRel().toString())
                .body(scriptService.getBodyById(id));
    }

    @GetMapping(value = "/{id}/result", produces = {"text/plain"})
    public ResponseEntity<String> getResult(@PathVariable Integer id) {
        return ResponseEntity
                .ok()
                .header(HttpHeaders.LINK,
                        entityLinks.linkForItemResource(ScriptReadDto.class, id).withSelfRel().toString())
                .body(scriptService.getResultById(id));
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
    @PostMapping
    @Operation(summary = "Evaluates passed script")
    public ResponseEntity<ScriptReadDto> evaluate(@NotBlank
                                                  @CheckScript
                                                  @RequestBody String jsCode) {

        Script script = scriptExecutionService.evaluate(jsCode);

        return ResponseEntity
                .status(202)
                .body(scriptReadMapper.toModel(script));
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
    @PostMapping("/{id}")
    @Operation(summary = "Terminates a specific script by its id. Returns 404(NOT_FOUND) if such a script was not found in the pool or queue")
    public ResponseEntity<?> stop(@PathVariable Integer id) {

        scriptExecutionService.stopById(id);
        return ResponseEntity
                .noContent()
                .header(HttpHeaders.LINK,
                        entityLinks.linkForItemResource(ScriptReadDto.class, id).withSelfRel().toString())
                .build();
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

        return ResponseEntity
                .noContent()
                .header(HttpHeaders.LINK, entityLinks.linkToCollectionResource(ScriptReadDto.class).withRel("allScripts").toString())
                .build();
    }

}
