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

import java.util.List;

@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class EmployeeServiceImpl implements EmployeeService{

    @Autowired
    private final EmployeeRepository employeeRepository;
    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee createEmployee(Employee employee) {
        validateEmployeeEmail(employee.getEmail());
        employee.setPasswordHash(hashPassword(employee.getPasswordHash()));
        return employeeRepository.save(employee);
    }

    private void validateEmployeeEmail(String email) {
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

        validateEmployeeEmail(employee.getEmail());
        findEmployee.setPasswordHash(hashPassword(employee.getPasswordHash()));
        findEmployee.setUsername(employee.getUsername());
        findEmployee.setEmail((employee.getEmail()));

        return employeeRepository.save(findEmployee);

    }

    @Override
    public void deleteEmployee(Long employeeId){
        employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee isn't exists with id:" + employeeId));

        employeeRepository.deleteById(employeeId);
    }


}
