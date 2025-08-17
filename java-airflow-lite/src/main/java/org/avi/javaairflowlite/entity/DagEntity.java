package org.avi.javaairflowlite.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "dags")
public class DagEntity {
    @Id
    private String id;
    private String schedule;

    @OneToMany(mappedBy = "dag", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DagTaskEntity> tasks = new ArrayList<>();

    public DagEntity() {}
    public DagEntity(String id, String schedule) { this.id = id; this.schedule = schedule; }
    public String getId() { return id; }
    public String getSchedule() { return schedule; }
    public List<DagTaskEntity> getTasks() { return tasks; }
    public void addTask(DagTaskEntity t) { tasks.add(t); t.setDag(this); }
}
