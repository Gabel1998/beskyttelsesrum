package org.ek.beskyttelsesrum.repository;

import org.ek.beskyttelsesrum.entity.Kommune;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KommuneRepository extends JpaRepository<Kommune, Long> {
}
