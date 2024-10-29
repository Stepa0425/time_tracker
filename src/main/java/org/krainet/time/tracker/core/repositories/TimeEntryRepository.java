package org.krainet.time.tracker.core.repositories;
import org.krainet.time.tracker.core.domain.TimeEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeEntryRepository extends JpaRepository<TimeEntry, Long> {
}
