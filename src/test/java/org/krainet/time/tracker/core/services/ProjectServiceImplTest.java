package org.krainet.time.tracker.core.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.krainet.time.tracker.core.domain.Project;
import org.krainet.time.tracker.core.exceptions.ResourceNotFoundException;
import org.krainet.time.tracker.core.repositories.ProjectRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ProjectServiceImplTest {
    @InjectMocks
    private ProjectServiceImpl projectService;
    @Mock
    private ProjectRepository projectRepository;

    @Test
    public void shouldReturnAllProjects(){
        var project = mock(Project.class);
        when(projectRepository.findAll()).thenReturn(List.of(project));

        List<Project> projects = projectService.getAllProjects();
        assertEquals(projects.size(), 1);
    }

    @Test
    public void shouldReturnSavedProject(){
        var project = mock(Project.class);
        when(project.getName()).thenReturn("Medicine app");
        when(project.getDescription()).thenReturn("Medicine app for cardiology");
        when(projectRepository.save(project)).thenReturn(new Project(1L,"Medicine app","Medicine app for cardiology"));

        Project savedProject = projectService.createProject(project);
        assertEquals(savedProject.getName(), project.getName());
        assertEquals(savedProject.getDescription(), project.getDescription());
    }

    @Test
    public void shouldReturnUpdatedProject(){
        var changedProject = mock(Project.class);
        when(changedProject.getName()).thenReturn("Medicine app");
        when(changedProject.getDescription()).thenReturn("Medicine app for cardiology");
        
        var foundProject = mock(Project.class);
        when(foundProject.getName()).thenReturn("Med App");
        when(foundProject.getDescription()).thenReturn("Medicine app cardiology");
        
        when(projectRepository.findById(1L)).thenReturn(Optional.of(foundProject));
        when(projectRepository.save(foundProject)).thenReturn(changedProject);

        Project updateProject = projectService.updateProject(1L, foundProject);
        assertEquals(updateProject.getName(), changedProject.getName());
        assertEquals(updateProject.getDescription(), changedProject.getDescription());
    }

    @Test
    public void shouldReturnExceptionWhenTryUpdateNotExistsProject(){
        var foundProject = mock(Project.class);
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> projectService.updateProject(1L, foundProject));

        assertEquals(exception.getMessage(), "Project isn't exists with id:1");
    }

    @Test
    public void shouldDeleteProject(){
        projectRepository.deleteById(1L);
        verify(projectRepository, times(1)).deleteById(1L);
    }



    @Test
    public void shouldReturnExceptionWhenTryDeleteNotExistsProject(){
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> projectService.deleteProject(1L));

        assertEquals(exception.getMessage(), "Project isn't exists with id:1");
    }

}
