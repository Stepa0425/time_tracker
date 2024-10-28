package org.krainet.time.tracker.core.services;

import org.krainet.time.tracker.core.domain.Task;


import java.util.List;

public interface TaskService {
    List<Task> getAllTasks();

    Task createTask(Task task);

    Task updateTask(Long taskId, Task updatedTask);

    void deleteTask(Long taskId);
}
