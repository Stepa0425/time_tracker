package org.krainet.time.tracker.rest;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.krainet.time.tracker.core.domain.TimeEntry;
import org.krainet.time.tracker.core.exceptions.ResourceNotFoundException;
import org.krainet.time.tracker.core.exceptions.TimeEntryAlreadyStartedException;
import org.krainet.time.tracker.core.exceptions.TimeEntryIsNotStartedException;
import org.krainet.time.tracker.core.services.TimeEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RestController
@RequestMapping("/time/tracker/api")
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class TimeEntryController {

    @Autowired
    private final TimeEntryService timeEntryService;

    @GetMapping("/timeEntries")
    public ResponseEntity<?> getTimeEntries() {
        try {
            List<TimeEntry> timeEntries = timeEntryService.getAllTimeEntries();
            return ResponseEntity.ok(timeEntries);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving timeEntries: " + e.getMessage());
        }
    }

    @PostMapping(path = "/timeEntries",
            consumes = "application/json",
            produces = "application/json")
    public ResponseEntity<?> createTimeEntry(@RequestBody TimeEntry timeEntry) {
        try {
            TimeEntry savedTimeEntry = timeEntryService.createTimeEntry(timeEntry);
            return new ResponseEntity<>(savedTimeEntry, HttpStatus.CREATED);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating timeEntry: " + e.getMessage());
        }
    }

    @PutMapping(path = "/timeEntries/{id}",
            consumes = "application/json",
            produces = "application/json")
    public ResponseEntity<?> updateTimeEntry(@PathVariable(value = "id") Long timeEntryId, @RequestBody TimeEntry updatedTimeEntry) {
        try {
            TimeEntry savedTimeEntry = timeEntryService.updateTimeEntry(timeEntryId, updatedTimeEntry);
            return ResponseEntity.ok(savedTimeEntry);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating timeEntry: " + e.getMessage());
        }
    }

    @DeleteMapping("/timeEntries/{id}")
    public ResponseEntity<?> deleteTimeEntry(@PathVariable("id") Long timeEntryId) {
        try {
            timeEntryService.deleteTimeEntry(timeEntryId);
            return ResponseEntity.ok("TimeEntry with id:" + timeEntryId + " deleted successfully!");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting timeEntry: " + e.getMessage());
        }
    }

    @GetMapping("/timeEntries/{id}/start")
    public ResponseEntity<?> startTimeEntry(@PathVariable("id") Long timeEntryId) {
        try {
            timeEntryService.startTimeEntry(timeEntryId);
            return ResponseEntity.ok("TimeEntry with id:" + timeEntryId + " started successfully!");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (TimeEntryAlreadyStartedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving timeEntries: " + e.getMessage());
        }
    }

    @GetMapping("/timeEntries/{id}/finish")
    public ResponseEntity<?> finishTimeEntry(@PathVariable("id") Long timeEntryId) {
        try {
            timeEntryService.finishTimeEntry(timeEntryId);
            return ResponseEntity.ok("TimeEntry with id:" + timeEntryId + " finished successfully!");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (TimeEntryIsNotStartedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving timeEntries: " + e.getMessage());
        }
    }

}
