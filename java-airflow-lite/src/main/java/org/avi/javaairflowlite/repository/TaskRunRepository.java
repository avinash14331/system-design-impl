package org.avi.javaairflowlite.repository;

import org.avi.javaairflowlite.entity.TaskRunEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRunRepository extends JpaRepository<TaskRunEntity, Long> {}
