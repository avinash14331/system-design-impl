package org.avi.javaairflowlite.factory;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.avi.javaairflowlite.entity.DagTaskEntity;
import org.avi.javaairflowlite.model.TaskResult;
import org.avi.javaairflowlite.task.Task;
import org.avi.javaairflowlite.task.impl.HttpTask;
import org.avi.javaairflowlite.task.impl.PrintTask;

import java.util.Map;

public class TaskFactory {
    private static final ObjectMapper M = new ObjectMapper();
    public static Task from(DagTaskEntity t) {
        try {
            Map cfg = M.readValue(t.getConfigJson(), Map.class);
            switch (t.getType()) {
                case "print": return new PrintTask((String) cfg.getOrDefault("message", ""));
                case "http": return new HttpTask((String) cfg.get("url"));
                default: return taskRun -> new TaskResult(true, "noop");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
