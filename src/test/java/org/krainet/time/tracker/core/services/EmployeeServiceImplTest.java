package org.krainet.time.tracker.core.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.krainet.time.tracker.core.domain.Employee;
import org.krainet.time.tracker.core.exceptions.EmployeeAlreadyExistsException;
import org.krainet.time.tracker.core.exceptions.ResourceNotFoundException;
import org.krainet.time.tracker.core.repositories.EmployeeRepository;
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
public class EmployeeServiceImplTest {

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Test
    public void shouldReturnAllEmployees() {
        var employee = mock(Employee.class);
        when(employeeRepository.findAll()).thenReturn(List.of(employee));

        List<Employee> employees = employeeService.getAllEmployees();
        assertEquals(employees.size(), 1);
    }

    @Test
    public void shouldReturnSavedEmployee() {
        var employee = mock(Employee.class);
        when(employee.getUsername()).thenReturn("Martin");
        when(employee.getEmail()).thenReturn("martinov@gmail.com");
        when(employeeRepository.save(employee)).thenReturn(employee);

        Employee savedEmployee = employeeService.createEmployee(employee);
        assertEquals(savedEmployee.getUsername(), employee.getUsername());
        assertEquals(savedEmployee.getEmail(), employee.getEmail());
    }

    @Test
    public void shouldReturnExceptionWhenCreateEmployeeWithEmailExists() {
        var employee = mock(Employee.class);
        when(employee.getEmail()).thenReturn("martinov@gmail.com");
        when(employeeRepository.findByEmail(employee.getEmail())).thenReturn(Optional.of(employee));

        EmployeeAlreadyExistsException exception = assertThrows(EmployeeAlreadyExistsException.class,
                () -> employeeService.createEmployee(employee));
        assertEquals(exception.getMessage(), "Employee with email: " + employee.getEmail() + " already exists. Please change the email.");
    }

    @Test
    public void shouldReturnUpdatedEmployee() {
        var changedEmployee = mock(Employee.class);
        when(changedEmployee.getUsername()).thenReturn("Martin");
        when(changedEmployee.getEmail()).thenReturn("martinov@gmail.com");

        var foundEmployee = mock(Employee.class);
        when(foundEmployee.getUsername()).thenReturn("Martin1024");
        when(foundEmployee.getEmail()).thenReturn("martinov1024@gmail.com");

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(foundEmployee));
        when(employeeRepository.save(foundEmployee)).thenReturn(changedEmployee);

        Employee updateEmployee = employeeService.updateEmployee(1L, foundEmployee);
        assertEquals(updateEmployee.getUsername(), changedEmployee.getUsername());
        assertEquals(updateEmployee.getEmail(), changedEmployee.getEmail());
    }

    @Test
    public void shouldReturnExceptionWhenTryUpdateNotExistsEmployee() {
        var foundEmployee = mock(Employee.class);
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> employeeService.updateEmployee(1L, foundEmployee));

        assertEquals(exception.getMessage(), "Employee isn't exists with id:1");
    }

    @Test
    public void shouldThrowExceptionWhenUpdatingEmployeeWithExistingEmail() {
        Long employeeId = 1L;
        var existingEmployee = mock(Employee.class);
        var updatedEmployee = mock(Employee.class);

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(existingEmployee));
        when(employeeRepository.findByEmail(updatedEmployee.getEmail())).thenReturn(Optional.of(existingEmployee));

        EmployeeAlreadyExistsException exception = assertThrows(EmployeeAlreadyExistsException.class,
                () -> employeeService.updateEmployee(employeeId, updatedEmployee));

        assertEquals("Employee with email: " + updatedEmployee.getEmail() + " already exists. Please change the email.", exception.getMessage());
    }

    @Test
    public void shouldDeleteEmployee() {
        employeeRepository.deleteById(1L);
        verify(employeeRepository, times(1)).deleteById(1L);
    }

    @Test
    public void shouldReturnExceptionWhenTryDeleteNotExistsEmployee() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> employeeService.deleteEmployee(1L));

        assertEquals(exception.getMessage(), "Employee isn't exists with id:1");
    }

}
