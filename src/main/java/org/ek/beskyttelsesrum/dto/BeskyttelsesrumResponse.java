package org.ek.beskyttelsesrum.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO til returnering af beskyttelsesrum data.
 */

@Getter
@Setter
public class BeskyttelsesrumResponse {
    private Long id;
    private String adresse;
    private String postalCode;
    private int kapacitet;
    private Double latitude;
    private Double longitude;
    private Long kommuneId;
    private String kommuneNavn;
}
