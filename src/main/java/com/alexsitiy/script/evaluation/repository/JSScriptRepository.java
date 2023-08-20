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
        if (filter.getStatuses() == null && sort.getSorts().isEmpty())
            return scripts.stream()
                    .toList();

        if (!sort.getSorts().isEmpty()) {
            Comparator<JSScript> comparator = null;
            for (String value : sort.getSorts()) {
                comparator = switch (value) {
                    case "id" ->
                            (comparator == null) ? Comparator.comparingInt(JSScript::getId) : comparator.thenComparingInt(JSScript::getId);
                    case "ID" ->
                            (comparator == null) ? Comparator.comparing(JSScript::getId, Comparator.reverseOrder()) : comparator.thenComparing(JSScript::getId, Comparator.reverseOrder());
                    case "time" ->
                            (comparator == null) ? Comparator.comparingLong(JSScript::getExecutionTime) : comparator.thenComparingLong(JSScript::getExecutionTime);
                    case "TIME" ->
                            (comparator == null) ? Comparator.comparing(JSScript::getExecutionTime, Comparator.reverseOrder()) : comparator.thenComparing(JSScript::getExecutionTime, Comparator.reverseOrder());
                    default -> null;
                };
            }

            if (filter.getStatuses() != null && comparator != null) {
                return scripts.stream()
                        .filter(script -> filter.getStatuses().contains(script.getStatus()))
                        .sorted(comparator)
                        .toList();
            } else if (filter.getStatuses() == null && comparator != null) {
                return scripts.stream()
                        .sorted(comparator)
                        .toList();
            }
        }

        return scripts.stream()
                .filter(jsScript -> filter.getStatuses().contains(jsScript.getStatus()))
                .toList();
    }

    public Optional<JSScript> findById(Integer id) {
        if (scripts.size() <= id)
            return Optional.empty();

        return Optional.ofNullable(scripts.get(id));
    }

    public JSScript create(String jsCode) {
        JSScript jsScript = new JSScript(
                Status.IN_QUEUE,
                jsCode,
                new ByteArrayOutputStream()
        );

        scripts.add(jsScript);
        int id = scripts.indexOf(jsScript);
        jsScript.setId(id);

        return jsScript;
    }

    public boolean delete(Integer id) {
        if (scripts.size() <= id) {
            return false;
        }
        scripts.remove(id.intValue());
        return true;
    }
}
