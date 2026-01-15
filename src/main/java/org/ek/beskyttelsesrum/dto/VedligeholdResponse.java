package org.ek.beskyttelsesrum.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * DTO til returnering af vedligeholdelse data.
 */
@Getter
@Setter
public class VedligeholdResponse {
    private Long id;
    private String beskrivelse;
    private LocalDate dato;
    private String udfoertAf;
    private String status;
    private Long beskyttelsesrumId;
    private String beskyttelsesrumAdresse;
    private String kommuneNavn;
}