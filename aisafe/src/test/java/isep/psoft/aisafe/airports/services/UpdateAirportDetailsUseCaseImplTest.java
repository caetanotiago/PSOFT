package isep.psoft.aisafe.airports.services;

import isep.psoft.aisafe.airports.domain.*;
import isep.psoft.aisafe.airports.dto.AirportContactRequest;
import isep.psoft.aisafe.airports.dto.OperatingHoursRequest;
import isep.psoft.aisafe.airports.repositories.AirportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateAirportDetailsUseCaseImplTest {

    @Mock
    private AirportRepository airportRepository;

    @InjectMocks
    private UpdateAirportDetailsUseCaseImpl useCase;

    private Airport oporto;

    @BeforeEach
    void setUp() {
        oporto = new Airport(
                new IATACode("OPO"),
                new AirportDetails("Francisco Sá Carneiro", "Porto", "Portugal", "Europe",
                        "Europe/Lisbon", new Coordinates(41.24, -8.68)),
                AirportState.OPERATIONAL,
                List.of(new Runway("18/36", 3480.0, "180/360")));
    }

    @Test
    void updates_only_operating_hours_when_contacts_absent() {
        when(airportRepository.findById(new IATACode("OPO"))).thenReturn(Optional.of(oporto));
        when(airportRepository.save(any(Airport.class))).thenAnswer(i -> i.getArgument(0));

        Airport result = useCase.updateDetails("OPO", new OperatingHoursRequest(true, null, null), null);

        assertNotNull(result.getOperatingHours());
        assertTrue(result.getOperatingHours().isOperates24Hours());
        assertTrue(result.getContacts().isEmpty());
    }

    @Test
    void updates_only_contacts_when_operating_hours_absent() {
        when(airportRepository.findById(new IATACode("OPO"))).thenReturn(Optional.of(oporto));
        when(airportRepository.save(any(Airport.class))).thenAnswer(i -> i.getArgument(0));

        Airport result = useCase.updateDetails("OPO", null,
                List.of(new AirportContactRequest("PHONE", "+351221234567", null)));

        assertEquals(1, result.getContacts().size());
        assertNull(result.getOperatingHours());
    }

    @Test
    void updates_both_fields_when_both_present() {
        when(airportRepository.findById(new IATACode("OPO"))).thenReturn(Optional.of(oporto));
        when(airportRepository.save(any(Airport.class))).thenAnswer(i -> i.getArgument(0));

        Airport result = useCase.updateDetails("OPO",
                new OperatingHoursRequest(false, LocalTime.of(6, 0), LocalTime.of(22, 0)),
                List.of(new AirportContactRequest("EMAIL", "ops@opo.example", "Operations")));

        assertFalse(result.getOperatingHours().isOperates24Hours());
        assertEquals(1, result.getContacts().size());
    }

    @Test
    void throws_bad_request_when_both_fields_absent() {
        assertThrows(IllegalArgumentException.class, () -> useCase.updateDetails("OPO", null, null));
        verify(airportRepository, never()).findById(any());
    }

    @Test
    void throws_not_found_for_missing_airport() {
        when(airportRepository.findById(new IATACode("XXX"))).thenReturn(Optional.empty());

        assertThrows(AirportNotFoundException.class, () ->
                useCase.updateDetails("XXX", new OperatingHoursRequest(true, null, null), null));
    }
}
