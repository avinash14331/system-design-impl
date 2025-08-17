package org.avi.javaairflowlite.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "task_runs")
public class TaskRunEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String taskId;
    private Instant startTime;
    private Instant endTime;
    private String status;

    @Lob
    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dag_run_id")
    private DagRunEntity run;

    public TaskRunEntity() {}
    public TaskRunEntity(DagRunEntity run, String taskId, Instant startTime, String status) {
        this.run = run; this.taskId = taskId; this.startTime = startTime; this.status = status;
    }
    public void setEndTime(Instant t) { this.endTime = t; }
    public void setStatus(String s) { this.status = s; }
    public void setMessage(String m) { this.message = m; }
    // Getter and setter for status
    public String getStatus() {
        return status;
    }

}
