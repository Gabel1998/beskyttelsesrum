package org.ek.beskyttelsesrum.repository;

import org.ek.beskyttelsesrum.entity.Beskyttelsesrum;
import org.ek.beskyttelsesrum.entity.Kommune;
import org.ek.beskyttelsesrum.entity.Vedligeholdelse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;


import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for VedligeholdRepository.
 */
@DataJpaTest
public class VedligeholdRepositoryTest {

    @Autowired
    private VedligeholdRepository vedligeholdRepository;

    @Autowired
    private BeskyttelsesrumRepository beskyttelsesrumRepository;

    @Autowired
    private KommuneRepository kommuneRepository;

    private Beskyttelsesrum testBeskyttelsesrum;

    @BeforeEach
    void setUp() {
        Kommune kommune = kommuneRepository.save(new Kommune("0101", "København"));
        testBeskyttelsesrum = beskyttelsesrumRepository.save(
                new Beskyttelsesrum("Rådhuspladsen 1", "1550", 200, 55.6761, 12.5683, kommune)
        );
    }

    @Test
    void shouldSaveAndFindVedligeholdelse() {
        Vedligeholdelse vedligeholdelse = new Vedligeholdelse(
                "Årlig inspektion",
                LocalDate.now(),
                "John Hansen",
                Vedligeholdelse.Status.PLANLAGT,
                testBeskyttelsesrum
        );
        vedligeholdRepository.save(vedligeholdelse);

        Vedligeholdelse found = vedligeholdRepository.findById(vedligeholdelse.getId()).orElse(null);

        assertNotNull(found);
        assertEquals("Årlig inspektion", found.getBeskrivelse());
        assertEquals(Vedligeholdelse.Status.PLANLAGT, found.getStatus());
    }

    @Test
    void shouldFindByBeskyttelsesrumId() {
        vedligeholdRepository.save(new Vedligeholdelse(
                "Inspektion", LocalDate.now(), "John", Vedligeholdelse.Status.PLANLAGT, testBeskyttelsesrum
        ));
        vedligeholdRepository.save(new Vedligeholdelse(
                "Reparation", LocalDate.now(), "Jane", Vedligeholdelse.Status.UDFOERT, testBeskyttelsesrum
        ));

        List<Vedligeholdelse> result = vedligeholdRepository.findByBeskyttelsesrumId(testBeskyttelsesrum.getId());

        assertEquals(2, result.size());
    }

    @Test
    void shouldFindByStatus() {
        vedligeholdRepository.save(new Vedligeholdelse(
                "Inspektion 1", LocalDate.now(), "John", Vedligeholdelse.Status.PLANLAGT, testBeskyttelsesrum
        ));
        vedligeholdRepository.save(new Vedligeholdelse(
                "Inspektion 2", LocalDate.now(), "Jane", Vedligeholdelse.Status.PLANLAGT, testBeskyttelsesrum
        ));
        vedligeholdRepository.save(new Vedligeholdelse(
                "Reparation", LocalDate.now(), "Bob", Vedligeholdelse.Status.UDFOERT, testBeskyttelsesrum
        ));

        List<Vedligeholdelse> planlagte = vedligeholdRepository.findByStatus(Vedligeholdelse.Status.PLANLAGT);
        List<Vedligeholdelse> udfoerte = vedligeholdRepository.findByStatus(Vedligeholdelse.Status.UDFOERT);

        assertEquals(2, planlagte.size());
        assertEquals(1, udfoerte.size());
    }

    @Test
    void shouldReturnEmptyListWhenNoVedligeholdelse() {
        List<Vedligeholdelse> result = vedligeholdRepository.findByBeskyttelsesrumId(testBeskyttelsesrum.getId());

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldTestKlargjortStatus() {
        Vedligeholdelse vedligeholdelse = new Vedligeholdelse(
                "Klargjort til brug",
                LocalDate.now(),
                "Admin",
                Vedligeholdelse.Status.KLARGJORT,
                testBeskyttelsesrum
        );
        vedligeholdRepository.save(vedligeholdelse);

        Vedligeholdelse found = vedligeholdRepository.findById(vedligeholdelse.getId()).orElse(null);

        assertNotNull(found);
        assertEquals(Vedligeholdelse.Status.KLARGJORT, found.getStatus());
    }
}