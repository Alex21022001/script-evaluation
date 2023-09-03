package com.alexsitiy.script.evaluation.service;

import com.alexsitiy.script.evaluation.exception.NoSuchScriptException;
import com.alexsitiy.script.evaluation.model.Script;
import com.alexsitiy.script.evaluation.model.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

/**
 * This class is used for CRUD operations over {@link Script}.
 * It utilizes {@link ConcurrentHashMap} to store scripts.
 * It also includes caching via {@link ConcurrentHashMap} for {@link Comparator} that is used for soring scripts.
 */
@Service
public class ScriptService {

    private static final Logger log = LoggerFactory.getLogger(ScriptService.class);

    private final Map<Integer, Script> scripts = new ConcurrentHashMap<>();
    private final Map<List<String>, Comparator<Script>> sortCache = new ConcurrentHashMap<>();


    /**
     * Finds all {@link Script} according to passing
     * statuses (filter) and sorts (sort) and return them in the {@link List}.
     *
     * @param statuses is used for filtering scripts.
     * @param sorts    is used for soring scripts.
     * @return {@link List<Script>} - the list of the found scripts.
     */
    public List<Script> findAll(Set<Status> statuses, List<String> sorts) {
        return scripts.values().stream()
                .filter(filteredBy(statuses))
                .sorted(sortedBy(sorts))
                .toList();
    }


    /**
     * Finds a specific {@link Script} by its id and return it.
     *
     * @param id id of the searching script.
     * @return {@link Script} - found script.
     * @throws NoSuchScriptException if the script with a given id was not found.
     */
    public Script findById(Integer id) {
        Script script = scripts.get(id);

        if (script == null)
            throw new NoSuchScriptException("There is no such a Script with id:%d".formatted(id), id);

        return script;
    }

    /**
     * Saves a given {@link Script} to storage.
     */
    public void save(Script script) {
        scripts.put(script.getId(), script);
    }

    /**
     * Delete the script by its id, but only if it has one of the
     * next statuses: COMPLETED,FAILED,INTERRUPTED.
     *
     * @param id the id of the script
     * @throws IllegalStateException if the script did have appropriate status.
     * @throws NoSuchScriptException if the script with a given id was not found.
     * @see Status
     */
    public void delete(Integer id) {
        Script script = findById(id);

        if (Status.isFinished(script.getStatus())) {
            scripts.remove(id, script);
            log.debug("Script {} was deleted", script);
        } else {
            throw new IllegalStateException("Couldn't delete the script with id:%d due to its inappropriate state".formatted(id));
        }
    }

    /**
     * Creates a Predicate for Script.
     *
     * @param filter Set of Status that is used for filtering scripts.
     * @return {@link Predicate}
     */
    private Predicate<? super Script> filteredBy(Set<Status> filter) {
        if (filter == null || filter.isEmpty())
            return script -> true;

        return script -> filter.contains(script.getStatus());
    }

    /**
     * Checks a comparator in cache and returns it, otherwise creates a new one and saves in cache.
     *
     * @param sort List of sorting values.
     * @return {@link Comparator} that is used for sorting Scripts.
     */
    private Comparator<Script> sortedBy(List<String> sort) {
        if (sort == null || sort.isEmpty()) {
            return (o1, o2) -> 0;
        }

        Comparator<Script> comparator = sortCache.get(sort);
        if (comparator != null) {
            return comparator;
        }

        for (String sortValue : sort) {
            comparator = createComparator(sortValue, comparator);
        }

        if (comparator == null) {
            return (o1, o2) -> 0;
        }

        sortCache.put(sort, comparator);
        return comparator;
    }

    private Comparator<Script> createComparator(String sortValue, Comparator<Script> comparator) {
        return switch (sortValue) {
            case "id" ->
                    (comparator == null) ? Comparator.comparingInt(Script::getId) : comparator.thenComparingInt(Script::getId);
            case "ID" ->
                    (comparator == null) ? Comparator.comparing(Script::getId, Comparator.reverseOrder()) : comparator.thenComparing(Script::getId, Comparator.reverseOrder());
            case "time" ->
                    (comparator == null) ? Comparator.comparingLong(Script::getExecutionTime) : comparator.thenComparingLong(Script::getExecutionTime);
            case "TIME" ->
                    (comparator == null) ? Comparator.comparing(Script::getExecutionTime, Comparator.reverseOrder()) : comparator.thenComparing(Script::getExecutionTime, Comparator.reverseOrder());
            case "scheduled" ->
                    (comparator == null) ? Comparator.comparing(Script::getScheduledTime) : comparator.thenComparing(Script::getScheduledTime);
            case "SCHEDULED" ->
                    (comparator == null) ? Comparator.comparing(Script::getScheduledTime, Comparator.reverseOrder()) : comparator.thenComparing(Script::getScheduledTime, Comparator.reverseOrder());
            default -> null;
        };
    }
}
