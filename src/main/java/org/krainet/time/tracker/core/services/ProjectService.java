package org.krainet.time.tracker.core.services;

import org.krainet.time.tracker.core.domain.Project;

import java.util.List;

public interface ProjectService {

    List<Project> getAllProjects();

    Project createProject(Project project);

    Project updateProject(Long projectId, Project updatedProject);

    void deleteProject(Long projectId);
}
