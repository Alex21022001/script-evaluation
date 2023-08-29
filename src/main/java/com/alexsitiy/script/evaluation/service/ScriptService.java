package com.alexsitiy.script.evaluation.service;

import com.alexsitiy.script.evaluation.exception.NoSuchScriptException;
import com.alexsitiy.script.evaluation.model.Script;
import com.alexsitiy.script.evaluation.model.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

///**
// * This class is used for CRUD operations over {@link com.alexsitiy.script.evaluation.model.JSScript},
// * exploits {@link JSScriptRepository} for it. It also returns representations of the {@link com.alexsitiy.script.evaluation.model.JSScript}
// * such as {@link JSScriptFullReadDto} and {@link JSScriptReadDto} by using {@link JSScriptFullReadMapper} and {@link JSScriptReadMapper}.
// */
@Service
public class ScriptService {

    private static final Logger log = LoggerFactory.getLogger(ScriptService.class);

    private final Map<Integer, Script> scripts = new ConcurrentHashMap<>();
    private final Map<List<String>, Comparator<Script>> sortCache = new ConcurrentHashMap<>();


    //        /**
//     * Look for all {@link com.alexsitiy.script.evaluation.model.JSScript} according to passing
//     * {@link JSScriptFilter} and {@link JSScriptSort} and return their representations in the {@link List}.
//     *
//     * @param filter is used for filtering scripts.
//     * @param sort   is used for soring scripts.
//     * @return {@link List<JSScriptFullReadDto>} - the list of the representations of the obtained scripts.
//     * @see JSScriptReadMapper
//     */
    public List<Script> findAll(Set<Status> statuses, List<String> sorts) {
        return scripts.values().stream()
                .filter(filteredBy(statuses))
                .sorted(sortedBy(sorts))
                .toList();
    }


    //
//    /**
//     * Look for a specific script by its id and return its representation {@link JSScriptFullReadDto}.
//     *
//     * @param id id of the searching script.
//     * @return {@link Optional>}
//     */
    public Script findById(Integer id) {
        Script script = scripts.get(id);

        if (script == null)
            throw new NoSuchScriptException("There is no such a Script with id:%d".formatted(id), id);

        return script;
    }

    public void save(Script script) {
        scripts.put(script.getId(), script);
    }

    /**
     * Delete the script by its id, but only if it has one of the
     * next statuses: COMPLETED,FAILED,INTERRUPTED.
     *
     * @param id the id of the script
     * @return true - if the script was deleted, false - not.
     * @see Status
     */
    public void delete(Integer id) {
        Script script = findById(id);
        Status status = script.getStatus();

        if (status == Status.COMPLETED || status == Status.INTERRUPTED || status == Status.FAILED) {
            scripts.remove(id, script);
            log.debug("Script {} was deleted", script);
        } else {
            throw new IllegalStateException("Couldn't delete the script with id:%d due to its inappropriate state".formatted(id));
        }
    }

    private Predicate<? super Script> filteredBy(Set<Status> filter) {
        if (filter == null || filter.isEmpty())
            return script -> true;

        return script -> filter.contains(script.getStatus());
    }

    private Comparator<Script> sortedBy(List<String> sort) {
        if (sort == null || sort.isEmpty()) {
            return (o1, o2) -> 0;
        }

        Comparator<Script> comparator = sortCache.get(sort);
        if (comparator != null) {
            return comparator;
        }

        for (String value : sort) {
            comparator = switch (value) {
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

        if (comparator == null) {
            return (o1, o2) -> 0;
        }

        sortCache.put(sort, comparator);
        return comparator;
    }
}
