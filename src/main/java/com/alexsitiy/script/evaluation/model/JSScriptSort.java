package com.alexsitiy.script.evaluation.model;

public class JSScriptSort {
    private boolean byId;
    private boolean byExecutionTime;

    public boolean isById() {
        return byId;
    }

    public void setById(boolean byId) {
        this.byId = byId;
    }

    public boolean isByExecutionTime() {
        return byExecutionTime;
    }

    public void setByExecutionTime(boolean byExecutionTime) {
        this.byExecutionTime = byExecutionTime;
    }
}
