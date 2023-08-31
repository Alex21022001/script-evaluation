package com.alexsitiy.script.evaluation.doc;

import com.alexsitiy.script.evaluation.dto.ScriptReadDto;
import com.alexsitiy.script.evaluation.model.Status;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.Set;

public interface ScriptController {

    @FindAllApiEndpoint
    ResponseEntity<CollectionModel<ScriptReadDto>> findAll(Set<Status> statuses, List<String> sorts);
    @FindByIdApiEndpoint
    ResponseEntity<ScriptReadDto> findById(Integer id, WebRequest request);

    ResponseEntity<String> getBody(Integer id, WebRequest request);

    ResponseEntity<String> getResult(Integer id, WebRequest request);

//    ResponseEntity<ScriptReadDto> evaluate(String jsCode);

    ResponseEntity<?> stop(Integer id);

    ResponseEntity<?> delete(Integer id);
}
