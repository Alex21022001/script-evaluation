package com.alexsitiy.script.evaluation.controller;

import com.alexsitiy.script.evaluation.doc.ScriptController;
import com.alexsitiy.script.evaluation.doc.annotation.EvaluateApiEndpoint;
import com.alexsitiy.script.evaluation.dto.ScriptReadDto;
import com.alexsitiy.script.evaluation.mapper.ScriptReadMapper;
import com.alexsitiy.script.evaluation.model.Script;
import com.alexsitiy.script.evaluation.model.Status;
import com.alexsitiy.script.evaluation.repository.ScriptRepository;
import com.alexsitiy.script.evaluation.service.ScriptService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.NonComposite;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.Set;

/**
 * The main Rest controller that is responsible for obtaining user's
 * request which can include the following:
 * <br/>
 * 1. Evaluates the JavaScript code.
 * <br/>
 * 2. Gets all scripts.
 * <br/>
 * 3. Gets a specific script by its id.
 * <br/>
 * 4. Gets script's body by script's id.
 * <br/>
 * 5. Gets script's result by script's id.
 * <br/>
 * 6. Stops a specific script by its id.
 * <br/>
 * 7. Deletes already finished script by its id.
 * <br/>
 * This controller uses {@link EntityLinks} and {@linkplain org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport} to
 * build appropriate HATEOAS links in representations and Link headers. It also
 * implements {@link ScriptController} interface for creating Swagger API documentation in declarative way.
 * <br/>
 * It also includes Swagger descriptions that is useful for testing the API.
 *
 * @see com.alexsitiy.script.evaluation.model.Script
 * @see ScriptRepository
 * @see ScriptService
 * @see ScriptReadDto
 */
@SecurityRequirement(name = "Authorization",scopes = "api")
@RestController
@RequestMapping("/scripts")
@ExposesResourceFor(ScriptReadDto.class)
@Validated
public class ScriptRestController implements ScriptController {

    private final ScriptService scriptService;
    private final ScriptReadMapper scriptReadMapper;


    @Autowired
    public ScriptRestController(ScriptService scriptService,
                                ScriptReadMapper scriptReadMapper) {
        this.scriptService = scriptService;
        this.scriptReadMapper = scriptReadMapper;
    }

    /**
     * Finds all scripts according to passed statuses (filter) and sorts (sort) objects.
     * <br/>
     * Sorting includes the following options:
     * <br/>
     * 1. id - sorts by script's id (asc).
     * <br/>
     * 2. ID - sorts by script's id (desc).
     * <br/>
     * 3. time - sorts by script's execution time (asc).
     * <br/>
     * 4. TIME - sorts by script's execution time (desc).
     * <br/>
     * 5. scheduled - sorts by script's scheduled time (asc).
     * <br/>
     * 6. SCHEDULED - sorts by script's scheduled time (desc).
     * <br/>
     * Filtering available values: COMPLETED,FAILED,INTERRUPTED,EXECUTING,IN_QUEUE.
     * <p/>
     * After getting the List of scripts adds HATEOAS link to each of them via {@link ScriptReadMapper}
     * that expands {@link org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport}.
     *
     * @param statuses List of {@link Status} that is used for filtering scripts.
     * @param sorts    List of String that is used for sorting scripts.
     * @return {@link CollectionModel} that is used for HATEOAS representation of the collection,
     * it comprises {@link ScriptReadDto}. Also returns 200(OK) status code.
     * @see ScriptRepository
     */
    @GetMapping
    public CollectionModel<ScriptReadDto> findAll(@NonComposite
                                                  @RequestParam(value = "statuses", required = false) Set<Status> statuses,
                                                  @NonComposite
                                                  @RequestParam(value = "sorts", required = false) List<String> sorts) {

        List<ScriptReadDto> scriptReadDtoList = scriptService.findAll(statuses, sorts).stream()
                .map(scriptReadMapper::toModel)
                .toList();

        Link link = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ScriptRestController.class)
                .evaluate(null)).withRel("evaluate").withType("POST").withTitle("Evaluates a given JavaScript code");

        return CollectionModel.of(scriptReadDtoList, link);
    }

    /**
     * Finds {@linkplain com.alexsitiy.script.evaluation.model.Script} by its id. And returns
     * its representation {@link ScriptReadDto}, also adds HATEOAS links to it via
     * {@link ScriptReadMapper} and its method - toModelWithAllLinks().
     * <br/>
     * It also includes caching, It caches the response via Cache-Control and Last-Modified headers
     * and validates the cache everytime to ensure that script with a specified id still exists and was not deleted.
     *
     * @param id      script's id
     * @param request it's used for cache validation, setting Last-Modified header and returning 304(NOT-MODIFIED)
     *                status if cache is valid.
     * @return {@link ScriptReadDto} with 200(OK) or 304(NOT_MODIFIED) if cache is valid.
     * @see ScriptRepository
     */
    @GetMapping("/{id}")
    public ResponseEntity<ScriptReadDto> findById(@PathVariable Integer id,
                                                  WebRequest request) {
        Script script = scriptService.findById(id);

        if (request.checkNotModified(script.getLastModified().toEpochMilli()))
            return null;

        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.noCache().cachePrivate())
                .body(scriptReadMapper.toModelWithAllLinks(script));
    }

    /**
     * Finds Script's body by script's id. And returns it if exists.
     * It also adds HATEOAS self link to Link header via {@link EntityLinks}.
     * <br/>
     * It also includes caching, It caches the response via Cache-Control and ETag headers
     * and validates the cache everytime to ensure that script with a specified id still exists and was not deleted.
     *
     * @param id      script's id
     * @param request it's used for cache validation, setting ETag header and returning 304(NOT-MODIFIED)
     *                status if cache is valid.
     * @return {@link String} that is representation of the script's body with 200(OK) or 304(NOT_MODIFIED) if cache is valid.
     * @see ScriptRepository
     */
    @GetMapping(value = "/{id}/body", produces = {"text/plain"})
    public ResponseEntity<String> getBody(@PathVariable Integer id, WebRequest request) {
        Script script = scriptService.findById(id);

        if (request.checkNotModified(String.valueOf(script.getBody().hashCode())))
            return null;

        return ResponseEntity
                .ok()
                .header(HttpHeaders.LINK, scriptReadMapper.getSelfLink(id))
                .cacheControl(CacheControl.noCache().cachePrivate())
                .body(script.getBody());
    }

    /**
     * Finds Script's result by script's id. And returns it if exists.
     * It also adds HATEOAS self link to Link header via {@link EntityLinks}.
     * <br/>
     * It includes caching, It caches the response via Cache-Control and Last-Modified headers if script
     * has already finished and validates the cache everytime to ensure that script with a specified id still exists and was not deleted.
     *
     * @param id      script's id
     * @param request it's used for cache validation, setting ETag header and returning 304(NOT-MODIFIED)
     *                status if cache is valid.
     * @return {@link String} that is representation of the script's result with 200(OK) or 304(NOT_MODIFIED) if cache is valid.
     * @see ScriptRepository
     */
    @GetMapping(value = "/{id}/result", produces = {"text/plain"})
    public ResponseEntity<String> getResult(@PathVariable Integer id, WebRequest request) {
        Script script = scriptService.findById(id);

        String selfLink = scriptReadMapper.getSelfLink(id);
        if (Status.isFinished(script.getStatus())) {
            if (request.checkNotModified(script.getLastModified().toEpochMilli()))
                return null;

            return ResponseEntity
                    .ok()
                    .header(HttpHeaders.LINK, selfLink)
                    .cacheControl(CacheControl.noCache().cachePrivate())
                    .body(script.getResult());
        }

        return ResponseEntity
                .ok()
                .header(HttpHeaders.LINK, selfLink)
                .body(script.getResult());
    }

    /**
     * Sends a given JavaScript to evaluation, but before validating it via
     * {@link NotBlank}.
     * Returns {@link ScriptReadDto} that includes HATEOAS links
     * to related resources.
     *
     * @param jsCode JavaScript code that need to be evaluated.
     * @return {@link ScriptReadDto} - that is a representation with HATEOAS links.
     * It also returns 202(ACCEPTED) status code.
     * @see ScriptService
     * @see ScriptReadMapper
     */
    @PostMapping
    @EvaluateApiEndpoint
    public ResponseEntity<ScriptReadDto> evaluate(@NotBlank
                                                  @RequestBody String jsCode) {
        Script script = scriptService.evaluate(jsCode);

        return ResponseEntity
                .status(202)
                .body(scriptReadMapper.toModel(script));
    }

    /**
     * Terminates the script by its id that is currently running. Returns
     * 204(NOT_CONTENT) with Link header that contains self HATEOAS link.
     *
     * @param id running script's id.
     * @return 204(NO_CONTENT).
     * @see com.alexsitiy.script.evaluation.model.Script
     * @see ScriptService
     * @see ScriptReadMapper
     */
    @PostMapping("/{id}")
    public ResponseEntity<?> stop(@PathVariable Integer id) {
        scriptService.stopById(id);

        return ResponseEntity
                .noContent()
                .header(HttpHeaders.LINK, scriptReadMapper.getSelfLink(id))
                .build();
    }

    /**
     * Deletes the script by its id and returns 204(NO_CONTENT) with Link header that contains self HATEOAS link.
     *
     * @param id running script's id.
     * @return 204(NO_CONTENT).
     * @see com.alexsitiy.script.evaluation.model.Script
     * @see ScriptRepository
     * @see ScriptReadMapper
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        scriptService.delete(id);

        return ResponseEntity
                .noContent()
                .header(HttpHeaders.LINK, scriptReadMapper.getAllScriptsLink())
                .build();
    }

}
