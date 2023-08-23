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

    public List<JSScriptReadDto> findAll(JSScriptFilter filter, JSScriptSort sort) {
        return jsScriptRepository.findAll(filter, sort).stream()
                .map(jsScriptReadMapper::map)
                .toList();
    }

    public Optional<JSScriptFullReadDto> findById(Integer id) {
        return jsScriptRepository.findById(id)
                .map(jsScriptFullReadMapper::map);
    }

    public boolean deleteExecutedTask(Integer id) {
        return jsScriptRepository.delete(id, List.of(Status.COMPLETED, Status.FAILED, Status.INTERRUPTED));
    }
}
