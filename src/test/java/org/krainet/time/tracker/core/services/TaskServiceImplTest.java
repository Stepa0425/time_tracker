package org.krainet.time.tracker.core.services;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.krainet.time.tracker.core.domain.Task;
import org.krainet.time.tracker.core.domain.Project;
import org.krainet.time.tracker.core.exceptions.ResourceNotFoundException;
import org.krainet.time.tracker.core.repositories.ProjectRepository;
import org.krainet.time.tracker.core.repositories.TaskRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    public void shouldReturnAllTasks() {
        var task = mock(Task.class);
        when(taskRepository.findAll()).thenReturn(List.of(task));

        List<Task> tasks = taskService.getAllTasks();
        assertEquals(tasks.size(), 1);
    }

    @Test
    public void shouldReturnSavedTask() {
        var task = mock(Task.class);
        var project = mock(Project.class);

        when(task.getTitle()).thenReturn("Create registration module");
        when(task.getDescription()).thenReturn("description");
        when(task.getProject()).thenReturn(project);
        when(projectRepository.findById(task.getProject().getId())).thenReturn(Optional.of(project));
        when(taskRepository.save(task)).thenReturn(task);

        Task savedTask = taskService.createTask(task);
        assertEquals(savedTask.getTitle(), task.getTitle());
        assertEquals(savedTask.getDescription(), task.getDescription());
        assertEquals(project.getId(), savedTask.getProject().getId());
    }

    @Test
    public void shouldReturnExceptionWhenProjectNotExists() {
        var task = mock(Task.class);
        var project = mock(Project.class);

        when(task.getProject()).thenReturn(project);
        when(task.getProject().getId()).thenReturn(1L);
        when(projectRepository.findById(task.getProject().getId())).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> taskService.createTask(task));

        assertEquals("Project not found with id:1", exception.getMessage());
    }

    @Test
    public void shouldReturnUpdatedTask() {
        var project = mock(Project.class);
        var foundTask = mock(Task.class);
        var changedTask = mock(Task.class);

        when(foundTask.getProject()).thenReturn(project);

        when(changedTask.getTitle()).thenReturn("New Title");
        when(changedTask.getDescription()).thenReturn("New Description");
        when(changedTask.getProject()).thenReturn(project);

        when(projectRepository.findById(foundTask.getProject().getId())).thenReturn(Optional.of(project));
        when(taskRepository.findById(1L)).thenReturn(Optional.of(foundTask));
        when(taskRepository.save(foundTask)).thenReturn(changedTask);

        Task updatedTask = taskService.updateTask(1L, changedTask);
        assertEquals(changedTask.getTitle(), updatedTask.getTitle());
        assertEquals(changedTask.getDescription(), updatedTask.getDescription());
        assertEquals(changedTask.getProject(), updatedTask.getProject());
    }

    @Test
    public void shouldReturnExceptionWhenUpdateNotExistTask() {
        var changedTask = mock(Task.class);
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> taskService.updateTask(1L, changedTask));

        assertEquals("Task not found with id:1", exception.getMessage());
    }

    @Test
    public void shouldReturnExceptionWhenUpdateTaskWithNotExistProject() {
        var changedTask = mock(Task.class);
        var foundTask = mock(Task.class);
        var project = mock(Project.class);

        when(changedTask.getProject()).thenReturn(project);
        when(project.getId()).thenReturn(1L);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(foundTask));
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> taskService.updateTask(1L, changedTask));

        assertEquals("Project not found with id:1", exception.getMessage());
    }

    @Test
    public void shouldDeleteTask(){
        taskRepository.deleteById(1L);
        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    public void shouldReturnExceptionWhenDeleteNotExistsTask(){

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> taskService.deleteTask(1L));

        assertEquals(exception.getMessage(), "Task isn't exists with id:1");
    }
}
