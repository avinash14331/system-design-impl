package org.avi.javaairflowlite.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "dag_runs")
public class DagRunEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Instant startTime;
    private Instant endTime;
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dag_id")
    private DagEntity dag;

    @OneToMany(mappedBy = "run", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskRunEntity> tasks = new ArrayList<>();

    public DagRunEntity() {}
    public DagRunEntity(DagEntity dag, Instant startTime, String status) {
        this.dag = dag; this.startTime = startTime; this.status = status;
    }
    public Long getId() { return id; }
    public Instant getStartTime() { return startTime; }
    public void setEndTime(Instant t) { this.endTime = t; }
    public void setStatus(String s) { this.status = s; }
    public List<TaskRunEntity> getTasks() { return tasks; }
}
