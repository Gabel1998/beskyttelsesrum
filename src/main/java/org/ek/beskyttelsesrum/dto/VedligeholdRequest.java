package org.ek.beskyttelsesrum.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * DTO til oprettelse og opdatering af vedligeholdelse.
 */
@Getter
@Setter
public class VedligeholdRequest {
    private String beskrivelse;
    private LocalDate dato;
    private String udfoertAf;
    private String status;
    private Long beskyttelsesrumId;
}