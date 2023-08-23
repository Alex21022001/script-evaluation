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
import java.util.function.Predicate;

@Repository
public class JSScriptRepository {

    private final List<JSScript> scripts = new CopyOnWriteArrayList<>();

    public List<JSScript> findAll(JSScriptFilter filter, JSScriptSort sort) {
        return scripts.stream()
                .filter(filteredBy(filter))
                .sorted(sortedBy(sort))
                .toList();
    }

    public Optional<JSScript> findById(Integer id) {
        if (!isValidId(id))
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
        if (!isValidId(id)) {
            return false;
        }

        scripts.remove(id.intValue());
        return true;
    }

    public boolean delete(Integer id, List<Status> statuses) {
        return isValidId(id) && scripts.removeIf(script -> script.getId().equals(id) && statuses.contains(script.getStatus()));
    }

    private Predicate<? super JSScript> filteredBy(JSScriptFilter filter) {
        if (filter.getStatuses() == null)
            return script -> true;

        return script -> filter.getStatuses().contains(script.getStatus());
    }

    private Comparator<? super JSScript> sortedBy(JSScriptSort sort) {
        if (sort.getSorts().isEmpty()) {
            return (o1, o2) -> 0;
        }

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

        return comparator == null ?
                (o1, o2) -> 0 : comparator;
    }

    private boolean isValidId(Integer id) {
        return scripts.size() > id;
    }
}
