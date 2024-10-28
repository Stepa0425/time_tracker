package org.krainet.time.tracker.core.exceptions;

public class EmployeeAlreadyExistsException extends RuntimeException {
    public EmployeeAlreadyExistsException(String email) {
        super("Employee with email: " + email + " already exists. Please change the email.");
    }
}
