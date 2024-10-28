package org.krainet.time.tracker.core.services;

import org.krainet.time.tracker.core.domain.Employee;

import java.util.List;

public interface EmployeeService {

    List<Employee> getAllEmployees();

    Employee createEmployee(Employee employee);

    Employee updateEmployee(Long employeeId, Employee updatedEmployee);

    void deleteEmployee(Long employeeId);
}
