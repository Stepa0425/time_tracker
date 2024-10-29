package org.krainet.time.tracker.core.repositories;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class TimeEntryRepositoryTest {

    @Autowired
    private TimeEntryRepository timeEntryRepository;

    @Test
    public void injectedRepositoryNotNull() {
        assertNotNull(timeEntryRepository);
    }

}
