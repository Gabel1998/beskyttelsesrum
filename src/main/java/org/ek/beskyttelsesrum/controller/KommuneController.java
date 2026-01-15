package org.ek.beskyttelsesrum.controller;

import org.ek.beskyttelsesrum.dto.BeskyttelsesrumResponse;
import org.ek.beskyttelsesrum.entity.Kommune;
import org.ek.beskyttelsesrum.repository.KommuneRepository;
import org.ek.beskyttelsesrum.service.BeskyttelsesrumService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller til håndtering af kommuner.
 */
@RestController
@RequestMapping("/kommuner")
public class KommuneController {

    private final KommuneRepository kommuneRepository;
    private final BeskyttelsesrumService beskyttelsesrumService;

    public KommuneController(KommuneRepository kommuneRepository, BeskyttelsesrumService beskyttelsesrumService) {
        this.kommuneRepository = kommuneRepository;
        this.beskyttelsesrumService = beskyttelsesrumService;
    }

    /**
     * Henter alle kommuner.
     */
    @GetMapping
    public ResponseEntity<List<Kommune>> getAllKommuner() {
        return ResponseEntity.ok(kommuneRepository.findAll());
    }

    /**
     * Henter alle beskyttelsesrum i en given kommune.
     */
    @GetMapping("/{kommuneId}/rooms")
    public ResponseEntity<List<BeskyttelsesrumResponse>> getRoomsByKommune(@PathVariable Long kommuneId) {
        return ResponseEntity.ok(beskyttelsesrumService.findByKommuneId(kommuneId));
    }
}