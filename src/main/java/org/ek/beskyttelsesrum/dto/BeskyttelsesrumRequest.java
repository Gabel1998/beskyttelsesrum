package org.ek.beskyttelsesrum.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO til oprettelse og opdatering af beskyttelsesrum.
 */

@Getter
@Setter
public class BeskyttelsesrumRequest {
    private String adresse;
    private String postalCode;
    private int kapacitet;
    private Double latitude;
    private Double longitude;
    private Long kommuneId;
}
