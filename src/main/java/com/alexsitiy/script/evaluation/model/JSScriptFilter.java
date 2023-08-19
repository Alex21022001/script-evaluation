package com.alexsitiy.script.evaluation.model;

import java.util.List;

public class JSScriptFilter {
    private List<Status> statuses;

    public List<Status> getStatuses() {
        return statuses;
    }

    public JSScriptFilter(List<Status> statuses) {
        this.statuses = statuses;
    }
}
