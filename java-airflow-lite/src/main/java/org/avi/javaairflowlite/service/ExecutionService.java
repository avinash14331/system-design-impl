package org.avi.javaairflowlite.service;

import org.avi.javaairflowlite.entity.DagEntity;
import org.avi.javaairflowlite.entity.DagRunEntity;
import org.avi.javaairflowlite.entity.DagTaskEntity;
import org.avi.javaairflowlite.entity.TaskRunEntity;
import org.avi.javaairflowlite.factory.TaskFactory;
import org.avi.javaairflowlite.model.TaskResult;
import org.avi.javaairflowlite.repository.DagRepository;
import org.avi.javaairflowlite.task.Task;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
public class ExecutionService {
    private final DagRepository dagRepo;
    private final PersistenceService persistenceService;
    private final ExecutorService executor = Executors.newFixedThreadPool(8);

    public ExecutionService(DagRepository dagRepo, PersistenceService persistenceService) {
        this.dagRepo = dagRepo; this.persistenceService = persistenceService;
    }

    public DagRunEntity trigger(String dagId) {
//        DagEntity dag = dagRepo.findById(dagId).orElseThrow();
        DagEntity dag = dagRepo.getDagWithTasks(dagId);
        DagRunEntity run = persistenceService.startDagRun(dag);

        // build task map
        Map<String, DagTaskEntity> tasks = dag.getTasks().stream().collect(Collectors.toMap(DagTaskEntity::getTaskId, t -> t));
        Map<String, CompletableFuture<TaskRunEntity>> futures = new HashMap<>();

        for (DagTaskEntity t : tasks.values()) {
            futures.put(t.getTaskId(), createFuture(t, tasks, futures, run));
        }

        CompletableFuture.allOf(futures.values().toArray(new CompletableFuture[0]))
                .whenComplete((v, ex) -> {
                    // compute overall status
                    boolean anyFailed = futures.values().stream().map(CompletableFuture::join)
                            .anyMatch(tr -> !"SUCCESS".equals(tr.getStatus()));
                    persistenceService.completeDagRun(run, anyFailed ? "FAILED" : "SUCCESS");
                });

        return run;
    }

    private CompletableFuture<TaskRunEntity> createFuture(DagTaskEntity node,
                                                          Map<String, DagTaskEntity> all,
                                                          Map<String, CompletableFuture<TaskRunEntity>> futures,
                                                          DagRunEntity run) {
        List<String> upstream = node.getUpstream();
        if (upstream.isEmpty()) {
            return CompletableFuture.supplyAsync(() -> callTask(node, run), executor);
        }
        List<CompletableFuture<TaskRunEntity>> deps = upstream.stream().map(futures::get).collect(Collectors.toList());
        return CompletableFuture.allOf(deps.toArray(new CompletableFuture[0]))
                .thenApplyAsync(v -> {
                    for (var d : deps) {
                        TaskRunEntity tr = d.join(); if (!"SUCCESS".equals(tr.getStatus())) {
                            // short-circuit
                            TaskRunEntity t = persistenceService.startTask(run, node.getTaskId());
                            persistenceService.completeTask(t, false, "Upstream failed");
                            return t;
                        }
                    }
                    return callTask(node, run);
                }, executor);
    }

    private TaskRunEntity callTask(DagTaskEntity node, DagRunEntity run) {
        TaskRunEntity tr = persistenceService.startTask(run, node.getTaskId());
        try {
            Task task = TaskFactory.from(node);
            TaskResult res = task.execute(tr);
            persistenceService.completeTask(tr, res.isSuccess(), res.getMessage());
        } catch (Exception e) {
            persistenceService.completeTask(tr, false, e.getMessage());
        }
        return tr;
    }
}
