package org.ek.beskyttelsesrum.config;

import org.ek.beskyttelsesrum.entity.Beskyttelsesrum;
import org.ek.beskyttelsesrum.entity.Kommune;
import org.ek.beskyttelsesrum.entity.Vedligeholdelse;
import org.ek.beskyttelsesrum.repository.BeskyttelsesrumRepository;
import org.ek.beskyttelsesrum.repository.KommuneRepository;
import org.ek.beskyttelsesrum.repository.VedligeholdRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Profile("showroom")
@Order(2)
public class ShowroomDataSeeder implements CommandLineRunner {

    private final KommuneRepository kommuneRepository;
    private final BeskyttelsesrumRepository beskyttelsesrumRepository;
    private final VedligeholdRepository vedligeholdRepository;

    public ShowroomDataSeeder(KommuneRepository kommuneRepository,
                              BeskyttelsesrumRepository beskyttelsesrumRepository,
                              VedligeholdRepository vedligeholdRepository) {
        this.kommuneRepository = kommuneRepository;
        this.beskyttelsesrumRepository = beskyttelsesrumRepository;
        this.vedligeholdRepository = vedligeholdRepository;
    }

    @Override
    public void run(String... args) {
        if (beskyttelsesrumRepository.count() > 0) {
            return;
        }

        // Hent kommuner (allerede loaded af DataLoader)
        Kommune kbh = kommuneRepository.findAll().stream()
                .filter(k -> k.getKode().equals("0101")).findFirst().orElse(null);
        Kommune aarhus = kommuneRepository.findAll().stream()
                .filter(k -> k.getKode().equals("0751")).findFirst().orElse(null);
        Kommune odense = kommuneRepository.findAll().stream()
                .filter(k -> k.getKode().equals("0461")).findFirst().orElse(null);
        Kommune aalborg = kommuneRepository.findAll().stream()
                .filter(k -> k.getKode().equals("0851")).findFirst().orElse(null);
        Kommune frederiksberg = kommuneRepository.findAll().stream()
                .filter(k -> k.getKode().equals("0147")).findFirst().orElse(null);

        if (kbh == null) return;

        // København
        Beskyttelsesrum r1 = beskyttelsesrumRepository.save(
                new Beskyttelsesrum("Nørrebrogade 42", "2200", 250, 55.6901, 12.5528, kbh));
        Beskyttelsesrum r2 = beskyttelsesrumRepository.save(
                new Beskyttelsesrum("Vesterbrogade 100", "1620", 180, 55.6711, 12.5530, kbh));
        Beskyttelsesrum r3 = beskyttelsesrumRepository.save(
                new Beskyttelsesrum("Amagerbrogade 15", "2300", 320, 55.6614, 12.6042, kbh));
        Beskyttelsesrum r4 = beskyttelsesrumRepository.save(
                new Beskyttelsesrum("Østerbrogade 78", "2100", 150, 55.7050, 12.5770, kbh));

        // Aarhus
        Beskyttelsesrum r5 = beskyttelsesrumRepository.save(
                new Beskyttelsesrum("Søndergade 20", "8000", 200, 56.1530, 10.2039, aarhus));
        Beskyttelsesrum r6 = beskyttelsesrumRepository.save(
                new Beskyttelsesrum("Randersvej 85", "8200", 280, 56.1710, 10.2100, aarhus));

        // Odense
        Beskyttelsesrum r7 = beskyttelsesrumRepository.save(
                new Beskyttelsesrum("Vestergade 12", "5000", 190, 55.3961, 10.3883, odense));

        // Aalborg
        Beskyttelsesrum r8 = beskyttelsesrumRepository.save(
                new Beskyttelsesrum("Algade 33", "9000", 220, 57.0480, 9.9187, aalborg));
        Beskyttelsesrum r9 = beskyttelsesrumRepository.save(
                new Beskyttelsesrum("Boulevarden 15", "9000", 160, 57.0440, 9.9230, aalborg));

        // Frederiksberg
        if (frederiksberg != null) {
            Beskyttelsesrum r10 = beskyttelsesrumRepository.save(
                    new Beskyttelsesrum("Gammel Kongevej 55", "1850", 140, 55.6770, 12.5340, frederiksberg));
        }

        // Vedligeholdelse
        vedligeholdRepository.save(new Vedligeholdelse(
                "Årlig inspektion", LocalDate.now().minusDays(30), "Henrik Jensen",
                Vedligeholdelse.Status.UDFOERT, r1));
        vedligeholdRepository.save(new Vedligeholdelse(
                "Ventilationssystem tjek", LocalDate.now().minusDays(10), "Lars Petersen",
                Vedligeholdelse.Status.UDFOERT, r2));
        vedligeholdRepository.save(new Vedligeholdelse(
                "Nødbelysning udskiftning", LocalDate.now().plusDays(14), "Maria Nielsen",
                Vedligeholdelse.Status.PLANLAGT, r3));
        vedligeholdRepository.save(new Vedligeholdelse(
                "Vandforsyning test", LocalDate.now(), "Peter Andersen",
                Vedligeholdelse.Status.IGANGVAERENDE, r5));
        vedligeholdRepository.save(new Vedligeholdelse(
                "Branddør reparation", LocalDate.now().minusDays(5), "Søren Hansen",
                Vedligeholdelse.Status.KLARGJORT, r8));
        vedligeholdRepository.save(new Vedligeholdelse(
                "Kapacitetsudvidelse", LocalDate.now().plusDays(30), "Henrik Jensen",
                Vedligeholdelse.Status.PLANLAGT, r6));

        System.out.println("Showroom data loaded: 10 beskyttelsesrum, 6 vedligeholdelser");
    }
}
