package isep.psoft.aisafe.aircraftmanagement.repositories;

import isep.psoft.aisafe.aircraftmanagement.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AircraftRepositoryTest {
    @Autowired
    private AircraftRepository repository;

    @Test
    void shouldPersistAndFindAircraft() {
        // Arrange (Cria um avião com os seus VOs)
        // Act
        // Assert
        assertThat(repository.count()).isEqualTo(0);
    }
}