package org.ek.beskyttelsesrum.service;

import org.ek.beskyttelsesrum.dto.VedligeholdRequest;
import org.ek.beskyttelsesrum.dto.VedligeholdResponse;
import org.ek.beskyttelsesrum.entity.Beskyttelsesrum;
import org.ek.beskyttelsesrum.entity.Vedligeholdelse;
import org.ek.beskyttelsesrum.mapper.VedligeholdMapper;
import org.ek.beskyttelsesrum.repository.BeskyttelsesrumRepository;
import org.ek.beskyttelsesrum.repository.VedligeholdRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service til håndtering af vedligeholdelsesaktiviteter.
 */
@Service
public class VedligeholdService {

    private final VedligeholdRepository vedligeholdRepository;
    private final BeskyttelsesrumRepository beskyttelsesrumRepository;
    private final VedligeholdMapper mapper;

    public VedligeholdService(VedligeholdRepository vedligeholdRepository,
                              BeskyttelsesrumRepository beskyttelsesrumRepository,
                              VedligeholdMapper mapper) {
        this.vedligeholdRepository = vedligeholdRepository;
        this.beskyttelsesrumRepository = beskyttelsesrumRepository;
        this.mapper = mapper;
    }

    public List<VedligeholdResponse> findAll() {
        return vedligeholdRepository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    public Optional<VedligeholdResponse> findById(Long id) {
        return vedligeholdRepository.findById(id)
                .map(mapper::toResponse);
    }

    public List<VedligeholdResponse> findByBeskyttelsesrumId(Long beskyttelsesrumId) {
        return vedligeholdRepository.findByBeskyttelsesrumId(beskyttelsesrumId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    public Optional<VedligeholdResponse> create(VedligeholdRequest request) {
        return beskyttelsesrumRepository.findById(request.getBeskyttelsesrumId())
                .map(beskyttelsesrum -> {
                    Vedligeholdelse saved = vedligeholdRepository.save(mapper.toEntity(request, beskyttelsesrum));
                    return mapper.toResponse(saved);
                });
    }

    public Optional<VedligeholdResponse> update(Long id, VedligeholdRequest request) {
        Optional<Vedligeholdelse> existingOpt = vedligeholdRepository.findById(id);
        Optional<Beskyttelsesrum> beskyttelsesrumOpt = beskyttelsesrumRepository.findById(request.getBeskyttelsesrumId());

        if (existingOpt.isEmpty() || beskyttelsesrumOpt.isEmpty()) {
            return Optional.empty();
        }

        Vedligeholdelse existing = existingOpt.get();
        mapper.updateEntity(existing, request, beskyttelsesrumOpt.get());
        return Optional.of(mapper.toResponse(vedligeholdRepository.save(existing)));
    }

    public boolean delete(Long id) {
        if (!vedligeholdRepository.existsById(id)) {
            return false;
        }
        vedligeholdRepository.deleteById(id);
        return true;
    }
}