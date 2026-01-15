package org.ek.beskyttelsesrum.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Beskyttelsesrum
 * Hvert beskyttelsesrum tilhører en Kommune
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Beskyttelsesrum {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String adresse;
    private String postnummer;

    private int kapacitet;

    @ManyToOne
    @JoinColumn(name = "kommune_id")
    private Kommune kommune;

    public Beskyttelsesrum(String adresse, String postnummer, int kapacitet, Kommune kommune) {
        this.adresse = adresse;
        this.postnummer = postnummer;
        this.kapacitet = kapacitet;
        this.kommune = kommune;
    }
}
