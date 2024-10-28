package org.krainet.time.tracker.core.services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.krainet.time.tracker.core.domain.Project;
import org.krainet.time.tracker.core.domain.Task;
import org.krainet.time.tracker.core.exceptions.ResourceNotFoundException;
import org.krainet.time.tracker.core.repositories.ProjectRepository;
import org.krainet.time.tracker.core.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class TaskServiceImpl implements TaskService {

    @Autowired
    private final TaskRepository taskRepository;

    @Autowired
    private final ProjectRepository projectRepository;

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public Task createTask(Task task) {
        Project project = findProjectById(task.getProject().getId());
        task.setProject(project);
        return taskRepository.save(task);
    }

    private Project findProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id:" + id));

    }

    private Task findTaskById(Long id){
       return taskRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Task not found with id:" + id));
    }

    @Override
    public Task updateTask(Long taskId, Task updatedTask) {
        Task task = findTaskById(taskId);
        Project project = findProjectById(updatedTask.getProject().getId());

        task.setTitle(updatedTask.getTitle());
        task.setDescription(updatedTask.getDescription());
        task.setProject(project);
        return taskRepository.save(task);
    }

    @Override
    public void deleteTask(Long taskId) {
        taskRepository.findById(taskId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Task isn't exists with id:" + taskId));

        taskRepository.deleteById(taskId);
    }
}
