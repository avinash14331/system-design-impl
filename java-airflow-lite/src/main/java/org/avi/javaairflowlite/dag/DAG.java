package org.avi.javaairflowlite.dag;

import org.avi.javaairflowlite.model.TaskContext;
import org.avi.javaairflowlite.model.TaskNode;
import org.avi.javaairflowlite.model.TaskResult;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

public class DAG {
    private final String id;
    private final String schedule; // cron-like string (for quartz)
    private final Map<String, TaskNode> nodes = new LinkedHashMap<>();

    public DAG(String id, String schedule) {
        this.id = id; this.schedule = schedule;
    }
    public String getId() { return id; }
    public String getSchedule() { return schedule; }

    public void addTask(TaskNode node) { nodes.put(node.getId(), node); }
    public Collection<TaskNode> getTasks() { return nodes.values(); }

    // simple executor: resolve dependencies and run tasks using CompletableFutures
    public void run(ExecutorService executor) {
        System.out.println("DAG run started: " + id + " at " + new Date());
        TaskContext context = new TaskContext();

        Map<String, CompletableFuture<TaskResult>> futures = new HashMap<>();

        for (TaskNode node : nodes.values()) {
            futures.put(node.getId(), createFutureFor(node, futures, executor, context));
        }

        CompletableFuture.allOf(futures.values().toArray(new CompletableFuture[0]))
                .whenComplete((v, t) -> System.out.println("DAG run complete: " + id))
                .join();
    }

    private CompletableFuture<TaskResult> createFutureFor(TaskNode node,
                                                          Map<String, CompletableFuture<TaskResult>> futures,
                                                          ExecutorService executor,
                                                          TaskContext context) {
        List<String> upstream = node.getUpstream();
        if (upstream.isEmpty()) {
            return CompletableFuture.supplyAsync(() -> callTask(node, context), executor);
        }

        List<CompletableFuture<TaskResult>> deps = upstream.stream()
                .map(futures::get)
                .collect(Collectors.toList());

        return CompletableFuture.allOf(deps.toArray(new CompletableFuture[0]))
                .thenApplyAsync(v -> {
                    // if any upstream failed, short-circuit
                    for (CompletableFuture<TaskResult> f : deps) {
                        TaskResult r = f.join();
                        if (!r.isSuccess()) {
                            return new TaskResult(false, "Upstream failed");
                        }
                    }
                    return callTask(node, context);
                }, executor);
    }

    private TaskResult callTask(TaskNode node, TaskContext context) {
        try {
            System.out.println("Running task: " + node.getId());
            //comment to use spring persistence version
//            TaskResult r = node.getTask().execute(context);
            TaskResult r = node.getTask().execute(null);
            System.out.println("Task " + node.getId() + " finished -> " + r.isSuccess());
            return r;
        } catch (Exception e) {
            e.printStackTrace();
            return new TaskResult(false, e.getMessage());
        }
    }
}
