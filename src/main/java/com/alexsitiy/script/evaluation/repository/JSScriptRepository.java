package com.alexsitiy.script.evaluation.repository;

import com.alexsitiy.script.evaluation.model.JSScript;
import com.alexsitiy.script.evaluation.model.JSScriptFilter;
import com.alexsitiy.script.evaluation.model.JSScriptSort;
import com.alexsitiy.script.evaluation.model.Status;
import org.springframework.stereotype.Repository;

import java.io.ByteArrayOutputStream;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class JSScriptRepository {

    private final List<JSScript> scripts = new CopyOnWriteArrayList<>();

    public List<JSScript> findAll(JSScriptFilter filter, JSScriptSort sort) {
        if (filter.getStatuses() == null && !sort.isById() && !sort.isByExecutionTime())
            return scripts.stream()
                    .toList();


        return scripts.stream()
                .filter(jsScript -> filter.getStatuses().contains(jsScript.getStatus()))
                .toList();
    }

    public Optional<JSScript> findById(Integer id) {
        if (scripts.size() < id)
            return Optional.empty();

        return Optional.ofNullable(scripts.get(id));
    }

    public JSScript create(String jsCode) {
        JSScript jsScript = new JSScript(
                null,
                Status.IN_QUEUE,
                null,
                jsCode,
                new ByteArrayOutputStream(),
                new ByteArrayOutputStream()
        );
        scripts.add(jsScript);
        int id = scripts.indexOf(jsScript);
        jsScript.setId(id);

        return jsScript;
    }
}
