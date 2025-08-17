package org.avi.javaairflowlite.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "dag_tasks")
public class DagTaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String taskId;
    private String type;

    // json config as text for simplicity
    @Lob
    private String configJson;

    @ElementCollection
    @CollectionTable(name = "dag_task_upstream", joinColumns = @JoinColumn(name = "task_id"))
    @Column(name = "upstream_task")
    private List<String> upstream = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dag_id")
    private DagEntity dag;

    public DagTaskEntity() {}
    public DagTaskEntity(String taskId, String type, String configJson) {
        this.taskId = taskId; this.type = type; this.configJson = configJson;
    }
    public String getTaskId() { return taskId; }
    public String getType() { return type; }
    public String getConfigJson() { return configJson; }
    public List<String> getUpstream() { return upstream; }
    public void setDag(DagEntity dag) { this.dag = dag; }
}
