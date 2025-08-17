package org.avi.javaairflowlite.task.impl;

import org.avi.javaairflowlite.entity.TaskRunEntity;
//import org.avi.javaairflowlite.model.TaskContext;
import org.avi.javaairflowlite.model.TaskResult;
import org.avi.javaairflowlite.task.Task;

public class PrintTask implements Task {
    private final String message;
    public PrintTask(String message) { this.message = message; }

    /**
     * Spring version implementation of task execution
     * @param taskRun
     * @return
     */
    @Override
    public TaskResult execute(TaskRunEntity taskRun) {
        System.out.println("[PrintTask] " + message);
        return new TaskResult(true, "printed");
    }

//    /**
//     * Non Spring version implementation of task execution
//     * @param context
//     * @return
//     * @throws Exception
//     */
//    @Override
//    public TaskResult execute(TaskContext context) throws Exception {
//        System.out.println("[PrintTask] " + message);
//        return new TaskResult(true, "printed");
//    }
}