package org.krainet.time.tracker.core.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.krainet.time.tracker.core.domain.Employee;
import org.krainet.time.tracker.core.domain.Task;
import org.krainet.time.tracker.core.domain.TimeEntry;
import org.krainet.time.tracker.core.exceptions.ResourceNotFoundException;
import org.krainet.time.tracker.core.repositories.EmployeeRepository;
import org.krainet.time.tracker.core.repositories.TaskRepository;
import org.krainet.time.tracker.core.repositories.TimeEntryRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;


@ExtendWith(MockitoExtension.class)
public class TimeEntryServiceImplTest {

    @InjectMocks
    private TimeEntryServiceImpl timeEntryService;

    @Mock
    private TimeEntryRepository timeEntryRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private TaskRepository taskRepository;

    @Test
    public void shouldReturnAllTimeEntries() {
        var timeEntry = mock(TimeEntry.class);
        when(timeEntryRepository.findAll()).thenReturn(List.of(timeEntry));

        List<TimeEntry> timeEntries = timeEntryService.getAllTimeEntries();
        assertEquals(timeEntries.size(), 1);
    }

    @Test
    public void shouldReturnSavedTimeEntry() {
        var timeEntry = mock(TimeEntry.class);
        var task = mock(Task.class);
        var employee = mock(Employee.class);

        when(timeEntry.getEmployee()).thenReturn(employee);
        when(timeEntry.getTask()).thenReturn(task);
        when(employee.getId()).thenReturn(1L);
        when(task.getId()).thenReturn(1L);
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(timeEntryRepository.save(timeEntry)).thenReturn(timeEntry);

        TimeEntry savedTimeEntry = timeEntryService.createTimeEntry(timeEntry);
        assertEquals(timeEntry.getTask(), savedTimeEntry.getTask());
        assertEquals(timeEntry.getEmployee(), savedTimeEntry.getEmployee());
        assertEquals(employee, savedTimeEntry.getEmployee());
        assertEquals(task, savedTimeEntry.getTask());
    }

    @Test
    public void shouldThrowExceptionWhenCreateTimeEntryWithEmployeeNotExists() {
        var timeEntry = mock(TimeEntry.class);
        var employee = mock(Employee.class);

        when(employee.getId()).thenReturn(1L);
        when(timeEntry.getEmployee()).thenReturn(employee);

        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class,
                () -> timeEntryService.createTimeEntry(timeEntry));

        assertEquals("Employee not found with id:1", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenCreateTimeEntryWithTaskNotExists() {
        var timeEntry = mock(TimeEntry.class);
        var task = mock(Task.class);
        var employee = mock(Employee.class);

        when(employee.getId()).thenReturn(1L);
        when(task.getId()).thenReturn(1L);
        when(timeEntry.getEmployee()).thenReturn(employee);
        when(timeEntry.getTask()).thenReturn(task);

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class,
                () -> timeEntryService.createTimeEntry(timeEntry));

        assertEquals("Task not found with id:1", exception.getMessage());
    }


    @Test
    public void shouldUpdateTimeEntry() {
        var existingTimeEntry = mock(TimeEntry.class);
        var updatedTimeEntry = mock(TimeEntry.class);
        var task = mock(Task.class);
        var employee = mock(Employee.class);

        when(updatedTimeEntry.getEmployee()).thenReturn(employee);
        when(updatedTimeEntry.getTask()).thenReturn(task);
        when(updatedTimeEntry.getStartTime()).thenReturn(LocalDateTime.now().minusHours(1));
        when(updatedTimeEntry.getEndTime()).thenReturn(LocalDateTime.now());
        when(updatedTimeEntry.getDuration()).thenReturn(45);

        when(employee.getId()).thenReturn(1L);
        when(task.getId()).thenReturn(1L);

        when(timeEntryRepository.findById(1L)).thenReturn(Optional.of(existingTimeEntry));
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(timeEntryRepository.save(any(TimeEntry.class))).thenReturn(existingTimeEntry);

        TimeEntry savedTimeEntry = timeEntryService.updateTimeEntry(1L, updatedTimeEntry);

        assertEquals(existingTimeEntry, savedTimeEntry);
    }

    @Test
    public void shouldThrowExceptionWhenUpdateTimeEntryTimeWithEntryNotExists() {
        var updatedTimeEntry = mock(TimeEntry.class);

        when(timeEntryRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class,
                () -> timeEntryService.updateTimeEntry(1L, updatedTimeEntry));

        assertEquals("TimeEntry isn't exists with id:1", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenUpdateTimeEntryWithEmployeeNotExists() {
        var existingTimeEntry = mock(TimeEntry.class);
        var employee = mock(Employee.class);

        when(employee.getId()).thenReturn(1L);

        var updatedTimeEntry = mock(TimeEntry.class);
        when(updatedTimeEntry.getEmployee()).thenReturn(employee);

        when(timeEntryRepository.findById(1L)).thenReturn(Optional.of(existingTimeEntry));
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class,
                () -> timeEntryService.updateTimeEntry(1L, updatedTimeEntry));

        assertEquals("Employee not found with id:1", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenUpdateTimeEntryWithTaskNotExists() {
        var existingTimeEntry = mock(TimeEntry.class);
        var employee = mock(Employee.class);
        var task = mock(Task.class);
        var updatedTimeEntry = mock(TimeEntry.class);

        when(employee.getId()).thenReturn(1L);
        when(task.getId()).thenReturn(1L);
        when(updatedTimeEntry.getEmployee()).thenReturn(employee);
        when(updatedTimeEntry.getTask()).thenReturn(task);

        when(timeEntryRepository.findById(1L)).thenReturn(Optional.of(existingTimeEntry));
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class,
                () -> timeEntryService.updateTimeEntry(1L, updatedTimeEntry));

        assertEquals("Task not found with id:1", exception.getMessage());
    }

    @Test
    public void shouldDeleteTimeEntry() {
        timeEntryRepository.deleteById(1L);
        verify(timeEntryRepository, times(1)).deleteById(1L);
    }

    @Test
    public void shouldReturnExceptionWhenDeleteNotExistsTimeEntry() {
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> timeEntryService.deleteTimeEntry(1L));

        assertEquals(exception.getMessage(), "TimeEntry isn't exists with id:1");
    }

    @Test
    public void shouldReturnStartedTimeEntry() {
        TimeEntry timeEntry = new TimeEntry();

        when(timeEntryRepository.findById(1L)).thenReturn(Optional.of(timeEntry));
        timeEntryService.startTimeEntry(1L);

        assertNotNull(timeEntry.getStartTime());
        verify(timeEntryRepository).save(timeEntry);
    }

    @Test
    public void shouldReturnExceptionWhenStartTimeEntryNotExists() {
        when(timeEntryRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> timeEntryService.startTimeEntry(1L));
        assertEquals("TimeEntry isn't exists with id:1", exception.getMessage());
    }

    @Test
    public void shouldReturnExceptionWhenStartEntryWithNotNullStartTime() {
        var timeEntry = mock(TimeEntry.class);
        when(timeEntry.getStartTime()).thenReturn(LocalDateTime.now());
        when(timeEntryRepository.findById(1L)).thenReturn(Optional.of(timeEntry));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> timeEntryService.startTimeEntry(1L));
        assertEquals("Time entry with id:1 is already started.", exception.getMessage());
    }

    @Test
    public void shouldReturnFinishedTimeEntry() {
        var timeEntry = new TimeEntry();
        timeEntry.setStartTime(LocalDateTime.now().minusMinutes(20));
        when(timeEntryRepository.findById(1L)).thenReturn(Optional.of(timeEntry));

        timeEntryService.finishTimeEntry(1L);

        assertNotNull(timeEntry.getEndTime());
        assertEquals(20, timeEntry.getDuration());
        verify(timeEntryRepository).save(timeEntry);
    }

    @Test
    public void shouldReturnExceptionWhenFinishTimeEntryNotExists() {
        when(timeEntryRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> timeEntryService.finishTimeEntry(1L));
        assertEquals("TimeEntry isn't exists with id:1", exception.getMessage());
    }

    @Test
    public void shouldReturnExceptionWhenStartEntryWithNullStartTime() {
        var timeEntry = mock(TimeEntry.class);
        when(timeEntry.getStartTime()).thenReturn(null);
        when(timeEntryRepository.findById(1L)).thenReturn(Optional.of(timeEntry));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> timeEntryService.finishTimeEntry(1L));
        assertEquals("Time entry with id:1 isn't started.", exception.getMessage());
    }

}
