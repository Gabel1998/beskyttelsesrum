package org.ek.beskyttelsesrum.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Repræsenterer en vedligeholdelsesaktivitet på et beskyttelsesrum.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Vedligeholdelse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String beskrivelse;

    private LocalDate dato;

    private String udfoertAf;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "beskyttelsesrum_id")
    private Beskyttelsesrum beskyttelsesrum;

    public Vedligeholdelse(String beskrivelse, LocalDate dato, String udfoertAf, Status status, Beskyttelsesrum beskyttelsesrum) {
        this.beskrivelse = beskrivelse;
        this.dato = dato;
        this.udfoertAf = udfoertAf;
        this.status = status;
        this.beskyttelsesrum = beskyttelsesrum;
    }

    /**
     * Status for vedligeholdelsesaktivitet.
     */
    public enum Status {
        PLANLAGT,
        IGANGVAERENDE,
        UDFOERT,
        KLARGJORT
    }
}