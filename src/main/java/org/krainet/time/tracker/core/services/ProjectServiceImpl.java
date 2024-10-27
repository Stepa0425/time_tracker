package org.krainet.time.tracker.core.services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.krainet.time.tracker.core.domain.Project;
import org.krainet.time.tracker.core.exceptions.ResourceNotFoundException;
import org.krainet.time.tracker.core.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class ProjectServiceImpl implements ProjectService{

    @Autowired
    private final ProjectRepository projectRepository;
    @Override
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @Override
    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    @Override
    public Project updateProject(Long projectId, Project project) {
        Project findProject = projectRepository.findById(projectId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Project isn't exists with id:" + projectId));

        findProject.setName(project.getName());
        findProject.setDescription((project.getDescription()));

        return projectRepository.save(findProject);

    }

    @Override
    public void deleteProject(Long projectId){
        projectRepository.findById(projectId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Project isn't exists with id:" + projectId));

        projectRepository.deleteById(projectId);
    }


}
