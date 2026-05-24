package isep.psoft.aisafe.airports.services;

import isep.psoft.aisafe.airports.domain.*;
import isep.psoft.aisafe.airports.repositories.AirportRepository;
import isep.psoft.aisafe.domain.shared.ModelDesignation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddCertificationUseCaseImplTest {

    @Mock
    private AirportRepository airportRepository;

    @InjectMocks
    private AddCertificationUseCaseImpl useCase;

    private Airport airport;

    @BeforeEach
    void setUp() {
        airport = new Airport(
                new IATACode("LIS"),
                new AirportDetails("Humberto Delgado", "Lisbon", "Portugal",
                        "Europe", "Europe/Lisbon", new Coordinates(38.77, -9.13)),
                AirportState.OPERATIONAL,
                List.of(new Runway("03/21", 3805.0, "030/210"))
        );
    }

    @Test
    void adds_certification_successfully() {
        when(airportRepository.findById(new IATACode("LIS"))).thenReturn(Optional.of(airport));
        when(airportRepository.save(any(Airport.class))).thenAnswer(i -> i.getArgument(0));

        Airport result = useCase.addCertification("LIS", "Boeing", "737-800");

        assertEquals(1, result.getCertifiedModels().size());
        assertTrue(result.getCertifiedModels().contains(new ModelDesignation("Boeing", "737-800")));
        verify(airportRepository).save(airport);
    }

    @Test
    void throws_not_found_for_missing_airport() {
        when(airportRepository.findById(any(IATACode.class))).thenReturn(Optional.empty());

        assertThrows(AirportNotFoundException.class,
                () -> useCase.addCertification("XXX", "Boeing", "737-800"));

        verify(airportRepository, never()).save(any());
    }

    @Test
    void throws_conflict_on_duplicate_certification() {
        airport.addCertification(new ModelDesignation("Boeing", "737-800"));
        when(airportRepository.findById(new IATACode("LIS"))).thenReturn(Optional.of(airport));

        assertThrows(ModelAlreadyCertifiedException.class,
                () -> useCase.addCertification("LIS", "Boeing", "737-800"));

        verify(airportRepository, never()).save(any());
    }

    @Test
    void iata_code_is_uppercased_before_lookup() {
        when(airportRepository.findById(new IATACode("LIS"))).thenReturn(Optional.of(airport));
        when(airportRepository.save(any(Airport.class))).thenAnswer(i -> i.getArgument(0));

        useCase.addCertification("lis", "Airbus", "A320");

        verify(airportRepository).findById(new IATACode("LIS"));
    }
}
