package org.ek.beskyttelsesrum.repository;

import org.ek.beskyttelsesrum.entity.Beskyttelsesrum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BeskyttelsesrumRepository extends JpaRepository<Beskyttelsesrum, Long> {

    List<Beskyttelsesrum> findAllByOrderByAdresseAsc();

    List<Beskyttelsesrum> findByKommuneId(Long kommuneId );

}
