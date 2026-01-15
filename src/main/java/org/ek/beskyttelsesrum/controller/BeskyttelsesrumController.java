package org.ek.beskyttelsesrum.controller;

import org.ek.beskyttelsesrum.entity.Beskyttelsesrum;
import org.ek.beskyttelsesrum.repository.BeskyttelsesrumRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller til håndtering af beskyttelsesrum.
 * Understøtter crud operationer
 */
@RestController
@RequestMapping("/rooms")
public class BeskyttelsesrumController {

    private final BeskyttelsesrumRepository beskyttelsesrumRepository;

    public BeskyttelsesrumController(BeskyttelsesrumRepository beskyttelsesrumRepository) {
        this.beskyttelsesrumRepository = beskyttelsesrumRepository;
    }

    /**
     * Opretter et nytt beskyttelsesrum
     * @param beskyttelsesrum Beskyttelsesrum objekt modtaget i request body
     * @return ResponseEntity
     */
    @PostMapping
    public ResponseEntity<Beskyttelsesrum> create(@RequestBody Beskyttelsesrum beskyttelsesrum) {
        Beskyttelsesrum saved = beskyttelsesrumRepository.save(beskyttelsesrum);
        return ResponseEntity.ok(saved);
    }

    /**
     * Opdaterer et eksisterende beskyttelsesrum     *
     * @param id Beskyttelsesrum id
     *           Beskyttelsesrum objekt modtaget i request body
     *           @return ResponseEntity
     */
    @PutMapping("/{id}")
    public ResponseEntity<Beskyttelsesrum> update(@PathVariable Long id, @RequestBody Beskyttelsesrum beskyttelsesrum) {
        if(!beskyttelsesrumRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        beskyttelsesrum.setId(id);
        Beskyttelsesrum updated = beskyttelsesrumRepository.save(beskyttelsesrum);
        return ResponseEntity.ok(updated);
    }

    /**
     * Sletter et beskyttelsesrum
     * @param id Beskyttelsesrum id
     * @return ResponseEntity<Void>
     */
     @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
         if (!beskyttelsesrumRepository.existsById(id)) {
             return ResponseEntity.notFound().build();
         }
         beskyttelsesrumRepository.deleteById(id);
         return ResponseEntity.noContent().build();
     }
}
