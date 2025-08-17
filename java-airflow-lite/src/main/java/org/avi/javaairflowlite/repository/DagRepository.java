package org.avi.javaairflowlite.repository;

import jakarta.transaction.Transactional;
import org.avi.javaairflowlite.entity.DagEntity;
import org.avi.javaairflowlite.entity.DagTaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DagRepository extends JpaRepository<DagEntity, String> {
    @Transactional
    default DagEntity getDagWithTasks(String dagId) {
        DagEntity dag = this.findById(dagId).orElseThrow();
        dag.getTasks().size(); // forces initialization
        dag.getTasks().stream().forEach(t -> t.getUpstream().size());
        return dag;
    }

    @Query("SELECT d FROM DagEntity d LEFT JOIN FETCH d.tasks WHERE d.id = :id")
    Optional<DagEntity> findByIdWithTasks(@Param("id") String id);

    @Query("SELECT d FROM DagEntity d LEFT JOIN FETCH d.tasks t LEFT JOIN FETCH t.upstream")
    List<DagEntity> findAllWithTasksAndUpstream();

    @Query("SELECT DISTINCT d FROM DagEntity d LEFT JOIN FETCH d.tasks")
    List<DagEntity> findAllWithTasks();

    @Query("SELECT DISTINCT t FROM DagTaskEntity t LEFT JOIN FETCH t.upstream WHERE t.dag.id = :dagId")
    List<DagTaskEntity> findTasksWithUpstream(Long dagId);

    @Transactional
    default Optional<DagEntity> findWithTasksAndUpstreamsById(@Param("id") String id){
        Optional<DagEntity> dagEntityOptional = this.findByIdWithTasks(id);
        if (dagEntityOptional.isPresent()) {
            DagEntity dag = dagEntityOptional.get();
            dag.getTasks().forEach(task -> task.getUpstream().size());
        }
        return dagEntityOptional;
    }


}
