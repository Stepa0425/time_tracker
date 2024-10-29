package org.krainet.time.tracker.core.services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.krainet.time.tracker.core.domain.Employee;
import org.krainet.time.tracker.core.domain.Task;
import org.krainet.time.tracker.core.domain.TimeEntry;
import org.krainet.time.tracker.core.exceptions.ResourceNotFoundException;
import org.krainet.time.tracker.core.exceptions.TimeEntryAlreadyStartedException;
import org.krainet.time.tracker.core.exceptions.TimeEntryIsNotStartedException;
import org.krainet.time.tracker.core.repositories.EmployeeRepository;
import org.krainet.time.tracker.core.repositories.TaskRepository;
import org.krainet.time.tracker.core.repositories.TimeEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class TimeEntryServiceImpl implements TimeEntryService {

    @Autowired
    private final TimeEntryRepository timeEntryRepository;

    @Autowired
    private final TaskRepository taskRepository;

    @Autowired
    private final EmployeeRepository employeeRepository;

    @Override
    public List<TimeEntry> getAllTimeEntries() {
        return timeEntryRepository.findAll();
    }

    @Override
    public TimeEntry createTimeEntry(TimeEntry timeEntry) {
        Employee employee = findEmployeeById(timeEntry.getEmployee().getId());
        Task task = findTaskById(timeEntry.getTask().getId());

        timeEntry.setEmployee(employee);
        timeEntry.setTask(task);

        return timeEntryRepository.save(timeEntry);
    }

    private Employee findEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id:" + id));
    }

    private Task findTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id:" + id));
    }


    @Override
    public TimeEntry updateTimeEntry(Long timeEntryId, TimeEntry updatedTimeEntry) {
        TimeEntry findTimeEntry = timeEntryRepository.findById(timeEntryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("TimeEntry isn't exists with id:" + timeEntryId));

        Employee employee = findEmployeeById(updatedTimeEntry.getEmployee().getId());
        Task task = findTaskById(updatedTimeEntry.getTask().getId());

        findTimeEntry.setStartTime(updatedTimeEntry.getStartTime());
        findTimeEntry.setEndTime(updatedTimeEntry.getEndTime());
        findTimeEntry.setDuration(updatedTimeEntry.getDuration());
        findTimeEntry.setTask(task);
        findTimeEntry.setEmployee(employee);

        return timeEntryRepository.save(findTimeEntry);
    }

    @Override
    public void deleteTimeEntry(Long timeEntryId) {
        taskRepository.findById(timeEntryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("TimeEntry isn't exists with id:" + timeEntryId));

        taskRepository.deleteById(timeEntryId);
    }

    @Override
    public void startTimeEntry(Long timeEntryId) {
        TimeEntry findTimeEntry = timeEntryRepository.findById(timeEntryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("TimeEntry isn't exists with id:" + timeEntryId));

        if (findTimeEntry.getStartTime() != null) {
            throw new TimeEntryAlreadyStartedException(timeEntryId);
        }

        findTimeEntry.setStartTime(LocalDateTime.now());
        timeEntryRepository.save(findTimeEntry);
    }

    @Override
    public void finishTimeEntry(Long timeEntryId) {
        TimeEntry findTimeEntry = timeEntryRepository.findById(timeEntryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("TimeEntry isn't exists with id:" + timeEntryId));

        if (findTimeEntry.getStartTime() == null) {
            throw new TimeEntryIsNotStartedException(timeEntryId);
        }

        LocalDateTime startTimeEntry = findTimeEntry.getStartTime();
        LocalDateTime endTimeEntry = LocalDateTime.now();
        Duration duration = Duration.between(startTimeEntry, endTimeEntry);

        findTimeEntry.setEndTime(endTimeEntry);
        findTimeEntry.setDuration((int)duration.toMinutes());
        timeEntryRepository.save(findTimeEntry);
    }
}
