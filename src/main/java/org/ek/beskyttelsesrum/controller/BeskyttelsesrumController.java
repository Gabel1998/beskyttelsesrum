package org.ek.beskyttelsesrum.controller;

import org.ek.beskyttelsesrum.dto.BeskyttelsesrumRequest;
import org.ek.beskyttelsesrum.dto.BeskyttelsesrumResponse;
import org.ek.beskyttelsesrum.service.BeskyttelsesrumService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller til håndtering af beskyttelsesrum.
 * Delegerer forretningslogik til BeskyttelsesrumService.
 */
@RestController
@RequestMapping("/rooms")
public class BeskyttelsesrumController {

    private final BeskyttelsesrumService beskyttelsesrumService;

    public BeskyttelsesrumController(BeskyttelsesrumService beskyttelsesrumService) {
        this.beskyttelsesrumService = beskyttelsesrumService;
    }

    /**
     * Henter alle beskyttelsesrum.
     */
    @GetMapping
    public ResponseEntity<List<BeskyttelsesrumResponse>> getAll() {
        return ResponseEntity.ok(beskyttelsesrumService.findAll());
    }

    /**
     * Henter et beskyttelsesrum by id.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BeskyttelsesrumResponse> getById(@PathVariable Long id) {
        return beskyttelsesrumService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Opretter et nyt beskyttelsesrum.
     */
    @PostMapping
    public ResponseEntity<BeskyttelsesrumResponse> create(@RequestBody BeskyttelsesrumRequest request) {
        return beskyttelsesrumService.create(request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    /**
     * Opdaterer et eksisterende beskyttelsesrum.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BeskyttelsesrumResponse> update(@PathVariable Long id, @RequestBody BeskyttelsesrumRequest request) {
        return beskyttelsesrumService.update(id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Sletter et beskyttelsesrum.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (beskyttelsesrumService.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}