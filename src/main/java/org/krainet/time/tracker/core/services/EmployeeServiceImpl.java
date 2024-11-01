package org.krainet.time.tracker.core.services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.krainet.time.tracker.core.domain.Employee;
import org.krainet.time.tracker.core.exceptions.EmployeeAlreadyExistsException;
import org.krainet.time.tracker.core.exceptions.ResourceNotFoundException;
import org.krainet.time.tracker.core.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class EmployeeServiceImpl implements EmployeeService{

    @Autowired
    private final EmployeeRepository employeeRepository;

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee createEmployee(Employee employee) {
        findEmployeeWithEmail(employee.getEmail());

        if (employee.getRole() == null || !isValidRole(employee.getRole())) {
            throw new IllegalArgumentException("Invalid role: " + employee.getRole());
        }
        employee.setPasswordHash(hashPassword(employee.getPasswordHash()));
        return employeeRepository.save(employee);
    }

    private void findEmployeeWithEmail(String email) {
        employeeRepository.findByEmail(email)
                .ifPresent(existingEmployee -> {
                    throw new EmployeeAlreadyExistsException(email);
                });
    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    @Override
    public Employee updateEmployee(Long employeeId, Employee employee) {

        Employee findEmployee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee isn't exists with id:" + employeeId));


        findEmployeeWithEmail(employee.getEmail());

        if (employee.getRole() == null || !isValidRole(employee.getRole())) {
            throw new IllegalArgumentException("Invalid role: " + employee.getRole());
        }

        findEmployee.setPasswordHash(hashPassword(employee.getPasswordHash()));
        findEmployee.setUsername(employee.getUsername());
        findEmployee.setEmail((employee.getEmail()));
        findEmployee.setRole(employee.getRole());

        return employeeRepository.save(findEmployee);

    }

    @Override
    public void deleteEmployee(Long employeeId){
        employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee isn't exists with id:" + employeeId));

        employeeRepository.deleteById(employeeId);
    }

    private boolean isValidRole(Employee.Role role) {
        return role != null && Arrays.stream(Employee.Role.values())
                .anyMatch(validRole -> validRole.equals(role));
    }
}
