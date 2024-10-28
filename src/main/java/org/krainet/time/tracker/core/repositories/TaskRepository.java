package org.krainet.time.tracker.core.repositories;

import org.krainet.time.tracker.core.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
