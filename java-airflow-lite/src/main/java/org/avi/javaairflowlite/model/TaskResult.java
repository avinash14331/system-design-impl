package org.avi.javaairflowlite.model;

import lombok.Getter;

@Getter
public class TaskResult {
    private final boolean success;
    private final String message;

    public TaskResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

}
