package org.ek.beskyttelsesrum.mapper;


import org.ek.beskyttelsesrum.dto.BeskyttelsesrumRequest;
import org.ek.beskyttelsesrum.dto.BeskyttelsesrumResponse;
import org.ek.beskyttelsesrum.entity.Beskyttelsesrum;
import org.ek.beskyttelsesrum.entity.Kommune;
import org.springframework.stereotype.Component;

/**
 * Mapper klasse for Beskyttelsesrum konvertering mellem entity og dto.
 */
@Component
public class BeskyttelsesrumMapper {

    /**
     * Konverterer Beskyttelsesrum entity til Beskyttelsesrum dto.
     */
    public Beskyttelsesrum toEntity(BeskyttelsesrumRequest request, Kommune kommune){
        Beskyttelsesrum beskyttelsesrum = new Beskyttelsesrum(request.getAdresse(),
                request.getPostalCode(),
                request.getKapacitet(),
                request.getLatitude(),
                request.getLongitude(),
                kommune);
        return beskyttelsesrum;
    }

    /**
     * Opdaterer eksisterende entity med data fra BeskyttelsesrumRequest.
     */
    public void updateEntity(Beskyttelsesrum beskyttelsesrum, BeskyttelsesrumRequest request, Kommune kommune){
        beskyttelsesrum.setAdresse(request.getAdresse());
        beskyttelsesrum.setPostalCode(request.getPostalCode());
        beskyttelsesrum.setKapacitet(request.getKapacitet());
        beskyttelsesrum.setLatitude(request.getLatitude());
        beskyttelsesrum.setLongitude(request.getLongitude());
        beskyttelsesrum.setKommune(kommune);
    }

    /**
     * Konverterer entity til response dto.
     */
    public BeskyttelsesrumResponse toResponse(Beskyttelsesrum beskyttelsesrum){
        BeskyttelsesrumResponse response = new BeskyttelsesrumResponse();
        response.setId(beskyttelsesrum.getId());
        response.setAdresse(beskyttelsesrum.getAdresse());
        response.setPostalCode(beskyttelsesrum.getPostalCode());
        response.setKapacitet(beskyttelsesrum.getKapacitet());
        response.setLatitude(beskyttelsesrum.getLatitude());
        response.setLongitude(beskyttelsesrum.getLongitude());
        response.setKommuneId(beskyttelsesrum.getKommune().getId());
        response.setKommuneNavn(beskyttelsesrum.getKommune().getNavn());
        return response;

    }


}
