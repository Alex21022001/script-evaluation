package com.alexsitiy.script.evaluation.doc;

import com.alexsitiy.script.evaluation.doc.annotation.*;
import com.alexsitiy.script.evaluation.dto.ScriptReadDto;
import com.alexsitiy.script.evaluation.model.Status;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.Set;

@Tag(name = "Script controller", description = "Contains endpoints for manipulating scripts")
public interface ScriptController {

    @FindAllApiEndpoint
    ResponseEntity<CollectionModel<ScriptReadDto>> findAll(Set<Status> statuses, List<String> sorts);

    @FindByIdApiEndpoint
    ResponseEntity<ScriptReadDto> findById(Integer id, WebRequest request);

    @GetBodyApiEndpoint
    ResponseEntity<String> getBody(Integer id, WebRequest request);

    @GetResultApiEndpoint
    ResponseEntity<String> getResult(Integer id, WebRequest request);

    @StopApiEndpoint
    ResponseEntity<?> stop(Integer id);

    @DeleteApiEndpoint
    ResponseEntity<?> delete(Integer id);
}
