package org.avi.javaairflowlite.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TaskContext {
    private final Map<String, Object> data = new ConcurrentHashMap<>();
    public void put(String k, Object v) { data.put(k, v); }
    public Object get(String k) { return data.get(k); }
}
