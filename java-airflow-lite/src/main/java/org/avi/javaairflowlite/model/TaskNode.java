package org.avi.javaairflowlite.model;


import org.avi.javaairflowlite.task.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskNode {
    private final String id;
    private final Task task;
    private final List<String> upstream = new ArrayList<>();

    public TaskNode(String id, Task task) {
        this.id = id;
        this.task = task;
    }

    public String getId() { return id; }
    public Task getTask() { return task; }
    public List<String> getUpstream() { return upstream; }
    public void dependsOn(String upstreamId) { upstream.add(upstreamId); }
}

