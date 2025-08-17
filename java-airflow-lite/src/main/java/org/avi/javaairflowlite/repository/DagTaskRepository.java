package org.avi.javaairflowlite.repository;

import org.avi.javaairflowlite.entity.DagTaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DagTaskRepository extends JpaRepository<DagTaskEntity, Long> {
    @Query("SELECT t FROM DagTaskEntity t LEFT JOIN FETCH t.upstream WHERE t.id = :id")
    DagTaskEntity findWithUpstream(@Param("id") Long id);
}