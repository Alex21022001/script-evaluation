package com.alexsitiy.script.evaluation.model;

import java.util.List;

/**
 * The class is used for holding obtained values from the Request for script filtering.
 *
 * @see com.alexsitiy.script.evaluation.repository.JSScriptRepository
 */
public record JSScriptFilter(List<Status> statuses) {

}
