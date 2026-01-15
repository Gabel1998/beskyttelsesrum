package org.ek.beskyttelsesrum.service;

import org.ek.beskyttelsesrum.dto.BeskyttelsesrumRequest;
import org.ek.beskyttelsesrum.dto.BeskyttelsesrumResponse;
import org.ek.beskyttelsesrum.entity.Beskyttelsesrum;
import org.ek.beskyttelsesrum.entity.Kommune;
import org.ek.beskyttelsesrum.mapper.BeskyttelsesrumMapper;
import org.ek.beskyttelsesrum.repository.BeskyttelsesrumRepository;
import org.ek.beskyttelsesrum.repository.KommuneRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service-lag til håndtering af beskyttelsesrum.
 * Indeholder forretningslogik og koordinerer mellem controller og repository.
 */
@Service
public class BeskyttelsesrumService {

    private final BeskyttelsesrumRepository beskyttelsesrumRepository;
    private final KommuneRepository kommuneRepository;
    private final BeskyttelsesrumMapper mapper;

    public BeskyttelsesrumService(BeskyttelsesrumRepository beskyttelsesrumRepository,
                                  KommuneRepository kommuneRepository,
                                  BeskyttelsesrumMapper mapper) {
        this.beskyttelsesrumRepository = beskyttelsesrumRepository;
        this.kommuneRepository = kommuneRepository;
        this.mapper = mapper;
    }

    public List<BeskyttelsesrumResponse> findAll() {
        return beskyttelsesrumRepository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    public Optional<BeskyttelsesrumResponse> findById(Long id) {
        return beskyttelsesrumRepository.findById(id)
                .map(mapper::toResponse);
    }

    public List<BeskyttelsesrumResponse> findByKommuneId(Long kommuneId) {
        return beskyttelsesrumRepository.findByKommuneId(kommuneId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    public Optional<BeskyttelsesrumResponse> create(BeskyttelsesrumRequest request) {
        return kommuneRepository.findById(request.getKommuneId())
                .map(kommune -> {
                    Beskyttelsesrum saved = beskyttelsesrumRepository.save(mapper.toEntity(request, kommune));
                    return mapper.toResponse(saved);
                });
    }

    public Optional<BeskyttelsesrumResponse> update(Long id, BeskyttelsesrumRequest request) {
        Optional<Beskyttelsesrum> existingOpt = beskyttelsesrumRepository.findById(id);
        Optional<Kommune> kommuneOpt = kommuneRepository.findById(request.getKommuneId());

        if (existingOpt.isEmpty() || kommuneOpt.isEmpty()) {
            return Optional.empty();
        }

        Beskyttelsesrum existing = existingOpt.get();
        mapper.updateEntity(existing, request, kommuneOpt.get());
        return Optional.of(mapper.toResponse(beskyttelsesrumRepository.save(existing)));
    }

    public boolean delete(Long id) {
        if (!beskyttelsesrumRepository.existsById(id)) {
            return false;
        }
        beskyttelsesrumRepository.deleteById(id);
        return true;
    }
}