package org.ek.beskyttelsesrum.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Repræsenterer et beskyttelsesrum.
 * Hvert beskyttelsesrum tilhører én kommune.
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

    private String postalCode;

    private int kapacitet;

    private Double latitude;

    private Double longitude;

    @ManyToOne
    @JoinColumn(name = "kommune_id")
    private Kommune kommune;

    public Beskyttelsesrum(String adresse, String postalCode, int kapacitet, Double latitude, Double longitude, Kommune kommune) {
        this.adresse = adresse;
        this.postalCode = postalCode;
        this.kapacitet = kapacitet;
        this.latitude = latitude;
        this.longitude = longitude;
        this.kommune = kommune;
    }
}