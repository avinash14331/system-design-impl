package org.avi.javaairflowlite.service;

import org.avi.javaairflowlite.entity.DagEntity;
import org.avi.javaairflowlite.entity.DagRunEntity;
import org.avi.javaairflowlite.entity.TaskRunEntity;
import org.avi.javaairflowlite.repository.DagRunRepository;
import org.avi.javaairflowlite.repository.TaskRunRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class PersistenceService {
    private final DagRunRepository runRepo;
    private final TaskRunRepository taskRepo;

    public PersistenceService(DagRunRepository runRepo, TaskRunRepository taskRepo) {
        this.runRepo = runRepo; this.taskRepo = taskRepo;
    }

    public DagRunEntity startDagRun(DagEntity dag) {
        DagRunEntity r = new DagRunEntity(dag, Instant.now(), "RUNNING");
        return runRepo.save(r);
    }

    public void completeDagRun(DagRunEntity run, String status) {
        run.setEndTime(Instant.now()); run.setStatus(status); runRepo.save(run);
    }

    public TaskRunEntity startTask(DagRunEntity run, String taskId) {
        TaskRunEntity t = new TaskRunEntity(run, taskId, Instant.now(), "RUNNING");
        return taskRepo.save(t);
    }

    public void completeTask(TaskRunEntity task, boolean success, String message) {
        task.setEndTime(Instant.now()); task.setStatus(success ? "SUCCESS" : "FAILED"); task.setMessage(message);
        taskRepo.save(task);
    }
}
