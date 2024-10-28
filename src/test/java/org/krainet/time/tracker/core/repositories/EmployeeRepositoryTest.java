package org.krainet.time.tracker.core.repositories;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.krainet.time.tracker.core.domain.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    public void injectedRepositoryNotNull() {
        assertNotNull(employeeRepository);
    }

    @Test
    public void shouldReturnEmployeeByEmail() {
        Optional<Employee> employee = employeeRepository.findByEmail("petrov@gmail.com");
        assertTrue((employee.isPresent()));
        assertEquals(employee.get().getEmail(), "petrov@gmail.com");
    }

    @Test
    public void shouldReturnEmptyEmployee() {
        Optional<Employee> employee = employeeRepository.findByEmail("marchenko@gmail.com");
        assertTrue((employee.isEmpty()));
    }

}
