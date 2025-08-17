package org.avi.javaairflowlite.component;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.avi.javaairflowlite.entity.DagEntity;
import org.avi.javaairflowlite.entity.DagTaskEntity;
import org.avi.javaairflowlite.repository.DagRepository;
import org.hibernate.Hibernate;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class DagYamlLoader {
    private final DagRepository dagRepository;
    private final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    public DagYamlLoader(DagRepository dagRepository) { this.dagRepository = dagRepository; }

    @EventListener(ApplicationReadyEvent.class)
    public void load() throws Exception {
        var resolver = new PathMatchingResourcePatternResolver();
        var resources = resolver.getResources("classpath:/dags/*.yaml");
        for (var r : resources) {
            try (InputStream is = r.getInputStream()) {
                Map<String, Object> model = mapper.readValue(is, Map.class);
                String id = (String) model.get("id");
                String schedule = (String) model.get("schedule");
                if (id == null) continue;
                DagEntity dag;
                Optional<DagEntity> dagEntityOptional = dagRepository.findWithTasksAndUpstreamsById(id);

                if (dagEntityOptional.isPresent()) {
                    dag = dagEntityOptional.get();
//                    dag.getTasks().forEach(task -> task.getUpstream().size()); // forces load
//                    dag.getTasks().forEach(task -> Hibernate.initialize(task.getUpstream()));
                } else {
                    dag = new DagEntity(id, schedule);
                }
//                dagRepository.save(dag);

                // handle tasks
                List<Map<String, Object>> tasks = (List<Map<String, Object>>) model.get("tasks");
                if (tasks != null) {
                    // remove existing tasks and re-add (simple approach)
                    dag.getTasks().clear();
                    for (Map<String, Object> t : tasks) {
                        String tid = (String) t.get("id");
                        String type = (String) t.get("type");

                        ObjectMapper mapper = new ObjectMapper(); // JSON, no ---
                        String configJson = mapper.writeValueAsString(t);
                        DagTaskEntity taskEntity = new DagTaskEntity(tid, type, configJson);
                        Object dep = t.get("depends_on");
                        if (dep instanceof List) {
                            for (Object u : (List<?>) dep) taskEntity.getUpstream().add((String) u);
                        }
                        dag.addTask(taskEntity);
                    }
                    dagRepository.save(dag);
                }
            }
        }
    }
}
