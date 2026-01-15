package org.ek.beskyttelsesrum.controller;

import org.ek.beskyttelsesrum.dto.VedligeholdRequest;
import org.ek.beskyttelsesrum.dto.VedligeholdResponse;
import org.ek.beskyttelsesrum.service.VedligeholdService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller til håndtering af vedligeholdelsesaktiviteter.
 */
@RestController
@RequestMapping("/vedligehold")
public class VedligeholdController {

    private final VedligeholdService vedligeholdService;

    public VedligeholdController(VedligeholdService vedligeholdService) {
        this.vedligeholdService = vedligeholdService;
    }

    /**
     * Henter alle vedligeholdelsesaktiviteter.
     */
    @GetMapping
    public ResponseEntity<List<VedligeholdResponse>> getAll() {
        return ResponseEntity.ok(vedligeholdService.findAll());
    }

    /**
     * Henter vedligeholdelse by id.
     */
    @GetMapping("/{id}")
    public ResponseEntity<VedligeholdResponse> getById(@PathVariable Long id) {
        return vedligeholdService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Henter alle vedligeholdelsesaktiviteter for et beskyttelsesrum.
     */
    @GetMapping("/room/{beskyttelsesrumId}")
    public ResponseEntity<List<VedligeholdResponse>> getByBeskyttelsesrum(@PathVariable Long beskyttelsesrumId) {
        return ResponseEntity.ok(vedligeholdService.findByBeskyttelsesrumId(beskyttelsesrumId));
    }

    /**
     * Opretter en ny vedligeholdelsesaktivitet.
     */
    @PostMapping
    public ResponseEntity<VedligeholdResponse> create(@RequestBody VedligeholdRequest request) {
        return vedligeholdService.create(request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    /**
     * Opdaterer en eksisterende vedligeholdelsesaktivitet.
     */
    @PutMapping("/{id}")
    public ResponseEntity<VedligeholdResponse> update(@PathVariable Long id, @RequestBody VedligeholdRequest request) {
        return vedligeholdService.update(id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Sletter en vedligeholdelsesaktivitet.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (vedligeholdService.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}