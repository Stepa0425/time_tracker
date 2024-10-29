package org.krainet.time.tracker.core.exceptions;

public class TimeEntryAlreadyStartedException extends RuntimeException {
    public TimeEntryAlreadyStartedException(Long timeEntryId) {
        super("Time entry with id:" + timeEntryId + " is already started.");
    }
}
