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

/**
 * The repository that is used for CRUD operations over the {@link JSScript}.
 * It makes use of {@link CopyOnWriteArrayList} as the main thread-safe storage for scripts.
 */
@Repository
public class JSScriptRepository {

    private final List<JSScript> scripts = new CopyOnWriteArrayList<>();

    /**
     * Retrieve all scripts from List according to passed {@link JSScriptFilter} and {@link JSScriptSort}.
     * Utilizes {@link Comparator} for sorting and {@link Predicate} for filtering.
     *
     * @param filter is used for filtering scripts.
     * @param sort   is used for soring scripts.
     * @return {@link List} of {@link JSScript}
     */
    public List<JSScript> findAll(JSScriptFilter filter, JSScriptSort sort) {
        return scripts.stream()
                .filter(filteredBy(filter))
                .sorted(sortedBy(sort))
                .toList();
    }

    /**
     * Look for a specific script by its id and return it.
     *
     * @param id id of the searching script.
     * @return {@link Optional<JSScript>}
     */
    public Optional<JSScript> findById(Integer id) {
        if (!isValidId(id))
            return Optional.empty();

        return Optional.ofNullable(scripts.get(id));
    }

    /**
     *  Creates a {@link JSScript} with default params, using a given jsCode and
     *  adds it to the List.
     *
     * @param jsCode JavaScript code, will be set a body field in {@link JSScript}
     * @return {@link JSScript} - created script.
     * */
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
    /**
     * Delete the script by its id
     *
     * @param id the id of the script
     * @return true - if the script was deleted, false - not.
     */
    public boolean delete(Integer id) {
        if (!isValidId(id)) {
            return false;
        }

        scripts.remove(id.intValue());
        return true;
    }

    /**
     * Delete the script by its id according to the given statuses.
     *
     * @param id the id of the script
     * @return true - if the script was deleted, false - not.
     * @see Status
     */
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
