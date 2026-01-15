package org.ek.beskyttelsesrum.config;

import org.ek.beskyttelsesrum.entity.Kommune;
import org.ek.beskyttelsesrum.repository.KommuneRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * Indlæser kommune data fra Danmarks Adressers Web API ved opstart.
 * Data gemmes i databasen så systemet ikke er afhængigt af API'et efterfølgende.
 */
@Component
public class DataLoader implements CommandLineRunner {

    private final KommuneRepository kommuneRepository;

    public DataLoader(KommuneRepository kommuneRepository) {
        this.kommuneRepository = kommuneRepository;
    }


    @Override
    public void run(String... args) {
        // Tjek om kommuner allerede er loaded
        if (kommuneRepository.count() > 0) {
            System.out.println("Kommuner allerede loaded (" + kommuneRepository.count() + " stk), springer over.");
            return;
        }

        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = "https://api.dataforsyningen.dk/kommuner";

            List<Map<String, Object>> kommuner = restTemplate.getForObject(url, List.class);

            for (Map<String, Object> k : kommuner) {
                String kode = (String) k.get("kode");
                String navn = (String) k.get("navn");
                Kommune kommune = new Kommune(kode, navn);
                kommuneRepository.save(kommune);
            }

            System.out.println("Loaded " + kommuner.size() + " kommuner fra API");

        } catch (Exception e) {
            System.err.println("Fejlet at læse data fra API, bruger fallback data: " + e.getMessage());
            loadFallbackData();
        }
    }

    /**
     * Fallback data hvis API'et ikke virker.
     */
    private void loadFallbackData() {
        kommuneRepository.save(new Kommune("0101", "København"));
        kommuneRepository.save(new Kommune("0751", "Aarhus"));
        kommuneRepository.save(new Kommune("0461", "Odense"));
        kommuneRepository.save(new Kommune("0851", "Aalborg"));
        kommuneRepository.save(new Kommune("0147", "Frederiksberg"));
        kommuneRepository.save(new Kommune("0561", "Esbjerg"));
        kommuneRepository.save(new Kommune("0730", "Randers"));
        kommuneRepository.save(new Kommune("0621", "Kolding"));
        kommuneRepository.save(new Kommune("0615", "Horsens"));
        kommuneRepository.save(new Kommune("0630", "Vejle"));
        System.out.println("Loaded 10 fallback kommuner");
    }
}