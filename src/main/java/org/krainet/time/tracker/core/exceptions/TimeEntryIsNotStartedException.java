package org.krainet.time.tracker.core.exceptions;

public class TimeEntryIsNotStartedException extends RuntimeException {
    public TimeEntryIsNotStartedException(Long timeEntryId) {
        super("Time entry with id:" + timeEntryId + " isn't started.");
    }
}
