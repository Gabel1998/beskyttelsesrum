package org.ek.beskyttelsesrum.repository;

import org.ek.beskyttelsesrum.entity.Beskyttelsesrum;
import org.ek.beskyttelsesrum.entity.Kommune;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for BeskyttelsesrumRepository.
 */
@DataJpaTest
public class BeskyttelsesrumRepositoryTest {

    @Autowired
    private BeskyttelsesrumRepository beskyttelsesrumRepository;

    @Autowired
    private KommuneRepository kommuneRepository;

    @Test
    void shouldSaveAndFindBeskyttelsesrum() {
        Kommune kommune = kommuneRepository.save(new Kommune("0101", "København"));
        Beskyttelsesrum room = new Beskyttelsesrum("Rådhuspladsen 1", "1550", 200, 55.6761, 12.5683, kommune);
        beskyttelsesrumRepository.save(room);

        Beskyttelsesrum found = beskyttelsesrumRepository.findById(room.getId()).orElse(null);

        assertNotNull(found);
        assertEquals("Rådhuspladsen 1", found.getAdresse());
        assertEquals(200, found.getKapacitet());
        assertEquals(55.6761, found.getLatitude());
    }

    @Test
    void shouldFindByKommuneId() {
        Kommune kommune = kommuneRepository.save(new Kommune("0101", "København"));
        beskyttelsesrumRepository.save(new Beskyttelsesrum("Rådhuspladsen 1", "1550", 200, 55.6761, 12.5683, kommune));
        beskyttelsesrumRepository.save(new Beskyttelsesrum("Nørrebrogade 15", "2200", 150, 55.6901, 12.5534, kommune));

        List<Beskyttelsesrum> rooms = beskyttelsesrumRepository.findByKommuneId(kommune.getId());

        assertEquals(2, rooms.size());
    }

    @Test
    void shouldReturnEmptyListForKommuneWithNoRooms() {
        Kommune kommune = kommuneRepository.save(new Kommune("0101", "København"));

        List<Beskyttelsesrum> rooms = beskyttelsesrumRepository.findByKommuneId(kommune.getId());

        assertTrue(rooms.isEmpty());
    }
}