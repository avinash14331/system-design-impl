package org.avi.javaairflowlite.dag;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.avi.javaairflowlite.model.DagYamlModel;
//import org.avi.javaairflowlite.model.TaskContext;
import org.avi.javaairflowlite.model.TaskNode;
import org.avi.javaairflowlite.model.TaskResult;
import org.avi.javaairflowlite.task.Task;
import org.avi.javaairflowlite.task.impl.HttpTask;
import org.avi.javaairflowlite.task.impl.PrintTask;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class DagLoader {
    private final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    public DAG loadFromResource(String resourcePath) throws Exception {
        try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
            if (is == null) throw new IllegalArgumentException("Resource not found: " + resourcePath);
            DagYamlModel model = mapper.readValue(is, DagYamlModel.class);
            DAG dag = new DAG(model.id, model.schedule);

            // simple convention: tasks is list of maps: {id:..., type:..., depends_on: [..]}
            for (Map<String, Object> t : model.tasks) {
                String id = (String) t.get("id");
                String type = (String) t.get("type");
                Task task = createTaskFromType(type, t);
                TaskNode node = new TaskNode(id, task);
                Object dep = t.get("depends_on");
                if (dep instanceof List) {
                    ((List<?>)dep).forEach(u -> node.dependsOn((String)u));
                }
                dag.addTask(node);
            }

            return dag;
        }
    }

    private Task createTaskFromType(String type, Map<String, Object> config) {
        // for this skeleton, only "print" task is supported
        switch (type.toLowerCase()) {
            case "print":
                return new PrintTask((String) config.getOrDefault("message", "hello"));
            case "http":
                return new HttpTask(
                        (String) config.get("url")
                );
//            case "mysql-query":
//                return new MySqlQueryTask(
//                        (String) config.get("query"),
//                        (String) config.get("datasource")
//                );
            default:
                return ctx -> new TaskResult(true, "noop");
        }
    }
}

