package org.alejo2075.coordinator_service.repository;

import org.alejo2075.coordinator_service.model.Task;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends CrudRepository<Task, String> {
}
