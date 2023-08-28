package com.alexsitiy.script.evaluation.service;

import com.alexsitiy.script.evaluation.exception.NoSuchScriptException;
import com.alexsitiy.script.evaluation.model.Script;
import com.alexsitiy.script.evaluation.model.Status;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

///**
// * This class is used for CRUD operations over {@link com.alexsitiy.script.evaluation.model.JSScript},
// * exploits {@link JSScriptRepository} for it. It also returns representations of the {@link com.alexsitiy.script.evaluation.model.JSScript}
// * such as {@link JSScriptFullReadDto} and {@link JSScriptReadDto} by using {@link JSScriptFullReadMapper} and {@link JSScriptReadMapper}.
// */
@Service
public class ScriptService {

    private final Map<Integer, Script> scripts = new ConcurrentHashMap<>();


//    /**
//     * Look for all {@link com.alexsitiy.script.evaluation.model.JSScript} according to passing
//     * {@link JSScriptFilter} and {@link JSScriptSort} and return their representations in the {@link List}.
//     *
//     * @param filter is used for filtering scripts.
//     * @param sort   is used for soring scripts.
//     * @return {@link List<JSScriptFullReadDto>} - the list of the representations of the obtained scripts.
//     * @see JSScriptReadMapper
//     */
//    public List<?> findAll(JSScriptFilter filter, JSScriptSort sort) {
//        return null;
//    }
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
            throw new NoSuchScriptException("There is no such a Script with id:%d".formatted(id));

        return script;
    }

    public Script create(String body) {
        Script script = new Script(body);
        scripts.put(script.getId(), script);

        return script;
    }

    /**
     * Delete the script by its id, but only if it has one of the
     * next statuses: COMPLETED,FAILED,INTERRUPTED.
     *
     * @param id the id of the script
     * @return true - if the script was deleted, false - not.
     * @see Status
     */
    public boolean deleteExecutedTask(Integer id) {
        return false;
    }
}
