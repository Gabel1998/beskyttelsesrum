package org.ek.beskyttelsesrum.repository;

import org.ek.beskyttelsesrum.entity.Kommune;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test class for the KommuneRepository.
 *
 * This class contains unit tests to verify the functionality of the methods
 * implemented in the KommuneRepository. It ensures that the repository
 * performs as expected when interacting with the data layer.
 *
 * The test cases in this class validate the correct behavior of CRUD operations,
 * query methods, and any additional custom logic defined in the repository.
 */
@DataJpaTest
public class KommuneRepositoryTest {

    @Autowired
    private KommuneRepository kommuneRepository;

    @Test
    void shouldSaveAndFindKommune() {
        Kommune kommune = new Kommune("0101", "København");
        kommuneRepository.save(kommune);

        Kommune found = kommuneRepository.findById(kommune.getId()).orElse(null);

        assertNotNull(found);
        assertEquals("København", found.getNavn());
        assertEquals("0101", found.getKode());
    }

    @Test
    void shouldFindAllKommuner() {
        kommuneRepository.save(new Kommune("0101", "København"));
        kommuneRepository.save(new Kommune("0751", "Aarhus"));

        assertEquals(2, kommuneRepository.findAll().size());
    }
}
