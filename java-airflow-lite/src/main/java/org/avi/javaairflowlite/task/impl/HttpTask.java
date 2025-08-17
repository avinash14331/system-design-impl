package org.avi.javaairflowlite.task.impl;

import org.avi.javaairflowlite.entity.TaskRunEntity;
//import org.avi.javaairflowlite.model.TaskContext;
import org.avi.javaairflowlite.model.TaskResult;
import org.avi.javaairflowlite.task.Task;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpTask implements Task {
    private final String url;
    public HttpTask(String url) { this.url = url; }

//    @Override
//    public TaskResult execute(TaskContext context) throws Exception {
//        return getTaskResult();
//    }

    private TaskResult getTaskResult() throws java.io.IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest req = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        return new TaskResult(resp.statusCode() >= 200 && resp.statusCode() < 300,
                "status=" + resp.statusCode());
    }

    @Override
    public TaskResult execute(TaskRunEntity taskRun) throws Exception {
        return getTaskResult();
    }
}
