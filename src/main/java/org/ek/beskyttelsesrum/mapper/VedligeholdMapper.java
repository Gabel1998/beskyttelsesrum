package org.ek.beskyttelsesrum.mapper;

import org.ek.beskyttelsesrum.dto.VedligeholdRequest;
import org.ek.beskyttelsesrum.dto.VedligeholdResponse;
import org.ek.beskyttelsesrum.entity.Beskyttelsesrum;
import org.ek.beskyttelsesrum.entity.Vedligeholdelse;
import org.springframework.stereotype.Component;

/**
 * Mapper til konvertering mellem Vedligeholdelse entity og DTO'er.
 */
@Component
public class VedligeholdMapper {

    public Vedligeholdelse toEntity(VedligeholdRequest request, Beskyttelsesrum beskyttelsesrum) {
        Vedligeholdelse vedligeholdelse = new Vedligeholdelse();
        vedligeholdelse.setBeskrivelse(request.getBeskrivelse());
        vedligeholdelse.setDato(request.getDato());
        vedligeholdelse.setUdfoertAf(request.getUdfoertAf());
        vedligeholdelse.setStatus(Vedligeholdelse.Status.valueOf(request.getStatus()));
        vedligeholdelse.setBeskyttelsesrum(beskyttelsesrum);
        return vedligeholdelse;
    }

    public void updateEntity(Vedligeholdelse vedligeholdelse, VedligeholdRequest request, Beskyttelsesrum beskyttelsesrum) {
        vedligeholdelse.setBeskrivelse(request.getBeskrivelse());
        vedligeholdelse.setDato(request.getDato());
        vedligeholdelse.setUdfoertAf(request.getUdfoertAf());
        vedligeholdelse.setStatus(Vedligeholdelse.Status.valueOf(request.getStatus()));
        vedligeholdelse.setBeskyttelsesrum(beskyttelsesrum);
    }

    public VedligeholdResponse toResponse(Vedligeholdelse vedligeholdelse) {
        VedligeholdResponse response = new VedligeholdResponse();
        response.setId(vedligeholdelse.getId());
        response.setBeskrivelse(vedligeholdelse.getBeskrivelse());
        response.setDato(vedligeholdelse.getDato());
        response.setUdfoertAf(vedligeholdelse.getUdfoertAf());
        response.setStatus(vedligeholdelse.getStatus().name());
        response.setBeskyttelsesrumId(vedligeholdelse.getBeskyttelsesrum().getId());
        response.setBeskyttelsesrumAdresse(vedligeholdelse.getBeskyttelsesrum().getAdresse());
        response.setKommuneNavn(vedligeholdelse.getBeskyttelsesrum().getKommune().getNavn());
        return response;
    }
}