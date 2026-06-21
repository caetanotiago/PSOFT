package isep.psoft.aisafe.aircraftmanagement.repositories;

import isep.psoft.aisafe.aircraftmanagement.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AircraftRepositoryTest {

    @Autowired private TestEntityManager em;
    @Autowired private AircraftRepository repository;

    @Test
    void shouldPersistAndFindAircraft() {
        AircraftModel model = em.persistAndFlush(new AircraftModel(
                new ModelDesignation("Airbus", "A320"),
                new ModelSpecifications(180, 24000.0, 6100.0, 840.0)));

        RegistrationNumber reg = new RegistrationNumber("CS-TVA");
        Aircraft aircraft = new Aircraft(reg, model,
                new ManufacturingDate(LocalDate.of(2020, 1, 1)),
                new SeatingCapacity(180),
                new AircraftStatus("AVAILABLE"));
        em.persistAndFlush(aircraft);

        Optional<Aircraft> found = repository.findById(reg);

        assertThat(found).isPresent();
        assertThat(repository.count()).isEqualTo(1);
    }
}
