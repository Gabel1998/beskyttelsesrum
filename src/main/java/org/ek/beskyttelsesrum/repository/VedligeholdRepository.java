package org.ek.beskyttelsesrum.repository;

import org.ek.beskyttelsesrum.entity.Vedligeholdelse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository til håndtering af vedligeholdelsesaktiviteter.
 */
public interface VedligeholdRepository extends JpaRepository<Vedligeholdelse, Long> {

    List<Vedligeholdelse> findByBeskyttelsesrumId(Long beskyttelsesrumId);

    List<Vedligeholdelse> findByStatus(Vedligeholdelse.Status status);
}