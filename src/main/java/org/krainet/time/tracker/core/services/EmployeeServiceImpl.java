package org.krainet.time.tracker.core.services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.krainet.time.tracker.core.domain.Employee;
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
        String hashpw = BCrypt.hashpw(employee.getPasswordHash(), BCrypt.gensalt());
        employee.setPasswordHash(hashpw);
        return employeeRepository.save(employee);
    }

    @Override
    public Employee updateEmployee(Long employeeId, Employee employee) {
        Employee findEmployee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee isn't exists with id:" + employeeId));

        String hashpw = BCrypt.hashpw(employee.getPasswordHash(), BCrypt.gensalt());

        findEmployee.setPasswordHash(hashpw);
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
