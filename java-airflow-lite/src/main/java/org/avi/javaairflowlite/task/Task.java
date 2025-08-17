package org.avi.javaairflowlite.task;

import org.avi.javaairflowlite.entity.TaskRunEntity;
//import org.avi.javaairflowlite.model.TaskContext;
import org.avi.javaairflowlite.model.TaskResult;

public interface Task {
//    TaskResult execute(TaskContext context) throws Exception;
    TaskResult execute(TaskRunEntity taskRun) throws Exception;
}
