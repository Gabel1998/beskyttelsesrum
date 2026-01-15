package org.ek.beskyttelsesrum.repository;


import org.ek.beskyttelsesrum.entity.Beskyttelsesrum;
import org.ek.beskyttelsesrum.entity.Kommune;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for the BeskyttelsesrumRepository.
 */
@DataJpaTest
public class BeskyttelsesrumRepositoryTest {

    @Autowired
    private BeskyttelsesrumRepository beskyttelsesrumRepository;

    @Autowired
    private KommuneRepository kommuneRepository;

    @Test
    void shouldFindByKommuneId(){
        // Test implementation goes here
        Kommune kommune = kommuneRepository.save(new Kommune("0101", "København"));
        beskyttelsesrumRepository.save(new Beskyttelsesrum("Rådhuspladsen1", "1550", 200, kommune));
        beskyttelsesrumRepository.save(new Beskyttelsesrum("Nørrebrogade 15", "2200", 150, kommune));

        List<Beskyttelsesrum> rooms = beskyttelsesrumRepository.findByKommuneId(kommune.getId());

        assert(rooms.size() == 2);

    }

    @Test
    void shouldReturnEmptyListForKommuneWithNoRooms(){
        Kommune kommune = kommuneRepository.save(new Kommune("0101", "københavn"));

        List<Beskyttelsesrum> rooms = beskyttelsesrumRepository.findByKommuneId(kommune.getId());

        assertTrue(rooms.isEmpty());
    }
}
