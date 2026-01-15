package org.ek.beskyttelsesrum.controller;

import org.ek.beskyttelsesrum.dto.BeskyttelsesrumRequest;
import org.ek.beskyttelsesrum.dto.BeskyttelsesrumResponse;
import org.ek.beskyttelsesrum.entity.Beskyttelsesrum;
import org.ek.beskyttelsesrum.entity.Kommune;
import org.ek.beskyttelsesrum.mapper.BeskyttelsesrumMapper;
import org.ek.beskyttelsesrum.repository.BeskyttelsesrumRepository;
import org.ek.beskyttelsesrum.repository.KommuneRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller til håndtering af beskyttelsesrum.
 * Understøtter crud operationer
 */
@RestController
@RequestMapping("/rooms")
public class BeskyttelsesrumController {


    private final BeskyttelsesrumRepository beskyttelsesrumRepository;
    private final KommuneRepository kommuneRepository;
    private final BeskyttelsesrumMapper mapper;


    public BeskyttelsesrumController(BeskyttelsesrumRepository beskyttelsesrumRepository, KommuneRepository kommuneRepository, BeskyttelsesrumMapper mapper) {
        this.beskyttelsesrumRepository = beskyttelsesrumRepository;
        this.kommuneRepository = kommuneRepository;
        this.mapper = mapper;
    }


    /**
     * Henter alle beskyttelsesrum i databasen
     */
@GetMapping
public ResponseEntity<List<BeskyttelsesrumResponse>> getAll(){
    List<BeskyttelsesrumResponse> rooms = beskyttelsesrumRepository.findAll()
            .stream()
            .map(mapper::toResponse)
            .toList();

    return ResponseEntity.ok(rooms);
}

/**
 * Henter et beskyttelsesrum by ID
 */
@GetMapping("/{id}")
public ResponseEntity<BeskyttelsesrumResponse> getById(@PathVariable Long id){
    return beskyttelsesrumRepository.findById(id)
            .map(mapper::toResponse)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
}

/**
 * Opretter et nytt beskyttelsesrum
 */
@PostMapping
public ResponseEntity<BeskyttelsesrumResponse> create(@RequestBody BeskyttelsesrumRequest request){
    Kommune kommune = kommuneRepository.findById(request.getKommuneId()).orElseThrow(null);

    if(kommune == null){
        return ResponseEntity.badRequest().build();
    }

    Beskyttelsesrum beskyttelsesrum = mapper.toEntity(request, kommune);
    Beskyttelsesrum saved = beskyttelsesrumRepository.save(beskyttelsesrum);
    return ResponseEntity.ok(mapper.toResponse(saved));
}

/**
 * Opdaterer et eksisterende beskyttelsesrum
 */
@PutMapping("/{id}")
public ResponseEntity<BeskyttelsesrumResponse> update(@PathVariable Long id, @RequestBody BeskyttelsesrumRequest request){
    Beskyttelsesrum beskyttelsesrum = beskyttelsesrumRepository.findById(id).orElseThrow(null);
    if (beskyttelsesrum == null){
        return ResponseEntity.notFound().build();
    }
    Kommune kommune = kommuneRepository.findById(request.getKommuneId()).orElseThrow(null);
    if (kommune == null){
        return ResponseEntity.badRequest().build();
    }
    mapper.updateEntity(beskyttelsesrum, request, kommune);
    Beskyttelsesrum updated = beskyttelsesrumRepository.save(beskyttelsesrum);
    return ResponseEntity.ok(mapper.toResponse(updated));
}

/**
 * Sletter et beskyttelsesrum
 */
@DeleteMapping("/{id}")
public ResponseEntity<Void> delete(@PathVariable Long id){
    if (!beskyttelsesrumRepository.existsById(id)){
        return ResponseEntity.notFound().build();
    }
    beskyttelsesrumRepository.deleteById(id);
    return ResponseEntity.noContent().build();
}
//
//    public ResponseEntity<String> delete(String id) {
//
//    }
//
//    /**
//     * Opretter et nytt beskyttelsesrum
//     * @param beskyttelsesrum Beskyttelsesrum objekt modtaget i request body
//     * @return ResponseEntity
//     */
//    @PostMapping
//    public ResponseEntity<Beskyttelsesrum> create(@RequestBody Beskyttelsesrum beskyttelsesrum) {
//        Beskyttelsesrum saved = beskyttelsesrumRepository.save(beskyttelsesrum);
//        return ResponseEntity.ok(saved);
//    }
//
//    /**
//     * Opdaterer et eksisterende beskyttelsesrum     *
//     * @param id Beskyttelsesrum id
//     *           Beskyttelsesrum objekt modtaget i request body
//     *           @return ResponseEntity
//     */
//    @PutMapping("/{id}")
//    public ResponseEntity<Beskyttelsesrum> update(@PathVariable Long id, @RequestBody Beskyttelsesrum beskyttelsesrum) {
//        if(!beskyttelsesrumRepository.existsById(id)) {
//            return ResponseEntity.notFound().build();
//        }
//        beskyttelsesrum.setId(id);
//        Beskyttelsesrum updated = beskyttelsesrumRepository.save(beskyttelsesrum);
//        return ResponseEntity.ok(updated);
//    }
//
//    /**
//     * Sletter et beskyttelsesrum
//     * @param id Beskyttelsesrum id
//     * @return ResponseEntity<Void>
//     */
//     @DeleteMapping("/{id}")
//    public ResponseEntity<Void> delete(@PathVariable Long id) {
//         if (!beskyttelsesrumRepository.existsById(id)) {
//             return ResponseEntity.notFound().build();
//         }
//         beskyttelsesrumRepository.deleteById(id);
//         return ResponseEntity.noContent().build();
//     }
}
