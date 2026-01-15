package org.ek.beskyttelsesrum.controller;

import org.ek.beskyttelsesrum.entity.Beskyttelsesrum;
import org.ek.beskyttelsesrum.entity.Kommune;
import org.ek.beskyttelsesrum.repository.BeskyttelsesrumRepository;
import org.ek.beskyttelsesrum.repository.KommuneRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller til håndtering af kommuner
 */
@RestController
@RequestMapping("/kommuner")
public class KommuneController {

    private final BeskyttelsesrumRepository beskyttelsesrumRepository;
    private final KommuneRepository kommuneRepository;

    public KommuneController(BeskyttelsesrumRepository beskyttelsesrumRepository, KommuneRepository kommuneRepository) {
        this.beskyttelsesrumRepository = beskyttelsesrumRepository;
        this.kommuneRepository = kommuneRepository;
    }

    /**
     * Henter alle kommuner
     */
    @GetMapping
    public ResponseEntity<List<Kommune>> getAllKomuner(){
        return ResponseEntity.ok(kommuneRepository.findAll());
    }

    /**
     * Henter alle beskyttelsesrum i en given kommune
     */
    @GetMapping("/{kommuneId}/rooms")
    public ResponseEntity<List<Beskyttelsesrum>> getRoomsByKommune(@PathVariable Long kommuneId) {
        List<Beskyttelsesrum> rooms = beskyttelsesrumRepository.findByKommuneId(kommuneId);
        return ResponseEntity.ok(rooms);
    }
}
