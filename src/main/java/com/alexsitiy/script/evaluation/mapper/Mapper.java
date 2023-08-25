package com.alexsitiy.script.evaluation.mapper;

/**
 * The main interface for mapping Model to DTO and
 * vice versa.
 *
 * @param <F> the object that is used as a data for mapping.
 * @param <T> the object which {@link F} is mapped to.
 */
public interface Mapper<F, T> {

    /**
     *  Maps {@link F} to {@link T}.
     *
     * @param object the class from which {@link T} will be created.
     * @return {@link T} the object mapped from {@link F}.
     * */
    T map(F object);
}
