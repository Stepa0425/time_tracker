package org.krainet.time.tracker.core.repositories;

import org.krainet.time.tracker.core.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProjectRepository extends JpaRepository<Project, Long> {
}
