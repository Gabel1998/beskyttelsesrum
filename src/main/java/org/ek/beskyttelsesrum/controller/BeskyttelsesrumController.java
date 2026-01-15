package org.ek.beskyttelsesrum.controller;

import org.ek.beskyttelsesrum.entity.Beskyttelsesrum;
import org.ek.beskyttelsesrum.repository.BeskyttelsesrumRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/rooms")
public class BeskyttelsesrumController {

    private final BeskyttelsesrumRepository beskyttelsesrumRepository;

    public BeskyttelsesrumController(BeskyttelsesrumRepository beskyttelsesrumRepository) {
        this.beskyttelsesrumRepository = beskyttelsesrumRepository;
    }

    @PostMapping
    public ResponseEntity<Beskyttelsesrum> create(@RequestBody Beskyttelsesrum beskyttelsesrum) {
        Beskyttelsesrum saved = beskyttelsesrumRepository.save(beskyttelsesrum);
        return ResponseEntity.ok(saved);
    }
}
