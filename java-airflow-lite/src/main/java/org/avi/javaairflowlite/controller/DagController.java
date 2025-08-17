package org.avi.javaairflowlite.controller;


import org.avi.javaairflowlite.entity.DagEntity;
import org.avi.javaairflowlite.entity.DagRunEntity;
import org.avi.javaairflowlite.repository.DagRepository;
import org.avi.javaairflowlite.service.ExecutionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dags")
public class DagController {
    private final DagRepository dagRepo;
    private final ExecutionService exec;

    public DagController(DagRepository dagRepo, ExecutionService exec) { this.dagRepo = dagRepo; this.exec = exec; }

    @GetMapping
    public List<DagEntity> list() { return dagRepo.findAll(); }

    @GetMapping("/{id}")
    public DagEntity get(@PathVariable String id) {
        return dagRepo.findById(id).orElseThrow();
    }

    @PostMapping("/{id}/trigger")
    public DagRunEntity trigger(@PathVariable String id) {
        return exec.trigger(id);
    }
}
