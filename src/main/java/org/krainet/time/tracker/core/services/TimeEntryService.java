package org.krainet.time.tracker.core.services;

import org.krainet.time.tracker.core.domain.TimeEntry;

import java.util.List;

public interface TimeEntryService {

    List<TimeEntry> getAllTimeEntries();

    TimeEntry createTimeEntry(TimeEntry timeEntry);

    TimeEntry updateTimeEntry(Long timeEntryId, TimeEntry updatedTimeEntry);

   void deleteTimeEntry(Long timeEntryId);

   void startTimeEntry(Long timeEntryId);

   void finishTimeEntry(Long timeEntryId);
}
