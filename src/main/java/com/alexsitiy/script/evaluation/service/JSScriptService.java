package com.alexsitiy.script.evaluation.service;

import com.alexsitiy.script.evaluation.dto.JSScriptFullReadDto;
import com.alexsitiy.script.evaluation.dto.JSScriptReadDto;
import com.alexsitiy.script.evaluation.mapper.JSScriptFullReadMapper;
import com.alexsitiy.script.evaluation.mapper.JSScriptReadMapper;
import com.alexsitiy.script.evaluation.model.JSScriptFilter;
import com.alexsitiy.script.evaluation.model.JSScriptSort;
import com.alexsitiy.script.evaluation.model.Status;
import com.alexsitiy.script.evaluation.repository.JSScriptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * This class is used for CRUD operations over {@link com.alexsitiy.script.evaluation.model.JSScript},
 * exploits {@link JSScriptRepository} for it. It also returns representations of the {@link com.alexsitiy.script.evaluation.model.JSScript}
 * such as {@link JSScriptFullReadDto} and {@link JSScriptReadDto} by using {@link JSScriptFullReadMapper} and {@link JSScriptReadMapper}.
 */
@Service
public class JSScriptService {

    private final JSScriptRepository jsScriptRepository;

    private final JSScriptFullReadMapper jsScriptFullReadMapper;
    private final JSScriptReadMapper jsScriptReadMapper;


    @Autowired
    public JSScriptService(JSScriptRepository jsScriptRepository,
                           JSScriptFullReadMapper jsScriptFullReadMapper,
                           JSScriptReadMapper jsScriptReadMapper) {
        this.jsScriptRepository = jsScriptRepository;
        this.jsScriptFullReadMapper = jsScriptFullReadMapper;
        this.jsScriptReadMapper = jsScriptReadMapper;
    }

    /**
     * Look for all {@link com.alexsitiy.script.evaluation.model.JSScript} according to passing
     * {@link JSScriptFilter} and {@link JSScriptSort} and return their representations in the {@link List}.
     *
     * @param filter is used for filtering scripts.
     * @param sort   is used for soring scripts.
     * @return {@link List<JSScriptFullReadDto>} - the list of the representations of the obtained scripts.
     * @see JSScriptReadMapper
     */
    public List<JSScriptReadDto> findAll(JSScriptFilter filter, JSScriptSort sort) {
        return jsScriptRepository.findAll(filter, sort).stream()
                .map(jsScriptReadMapper::map)
                .toList();
    }

    /**
     * Look for a specific script by its id and return its representation {@link JSScriptFullReadDto}.
     *
     * @param id id of the searching script.
     * @return {@link Optional<JSScriptFullReadDto>}
     */
    public Optional<JSScriptFullReadDto> findById(Integer id) {
        return jsScriptRepository.findById(id)
                .map(jsScriptFullReadMapper::map);
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
        return jsScriptRepository.delete(id, List.of(Status.COMPLETED, Status.FAILED, Status.INTERRUPTED));
    }
}
