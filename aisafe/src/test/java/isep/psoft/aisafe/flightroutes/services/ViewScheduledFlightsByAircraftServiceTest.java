package isep.psoft.aisafe.flightroutes.services;

import isep.psoft.aisafe.aircraftmanagement.repositories.AircraftRepository;
import isep.psoft.aisafe.flightroutes.domain.ScheduledFlight;
import isep.psoft.aisafe.flightroutes.repositories.ScheduledFlightRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ViewScheduledFlightsByAircraftServiceTest {

    @Mock private ScheduledFlightRepository scheduledFlightRepository;
    @Mock private AircraftRepository aircraftRepository;

    @InjectMocks private ViewScheduledFlightsByAircraftService service;

    private static final String REG = "CS-TVA";

    @Test
    void ensureViewByAircraftThrows404WhenAircraftMissing() {
        when(aircraftRepository.existsByRegistrationNumber(any())).thenReturn(false);

        assertThrows(EntityNotFoundException.class,
                () -> service.viewByAircraft(REG, PageRequest.of(0, 10)));
        verify(scheduledFlightRepository, never()).findByAircraftRegistration(anyString(), any());
    }

    @Test
    void ensureViewByAircraftReturnsPageWhenAircraftExists() {
        when(aircraftRepository.existsByRegistrationNumber(any())).thenReturn(true);
        Pageable pageable = PageRequest.of(0, 5);
        Page<ScheduledFlight> page = new PageImpl<>(List.of(mock(ScheduledFlight.class)), pageable, 1);
        when(scheduledFlightRepository.findByAircraftRegistration(REG, pageable)).thenReturn(page);

        Page<ScheduledFlight> result = service.viewByAircraft(REG, pageable);

        assertEquals(1, result.getTotalElements());
        verify(scheduledFlightRepository).findByAircraftRegistration(REG, pageable);
    }

    @Test
    void ensureGetByIdReturnsFlight() {
        ScheduledFlight flight = mock(ScheduledFlight.class);
        when(scheduledFlightRepository.findById("sf-1")).thenReturn(Optional.of(flight));

        assertSame(flight, service.getById("sf-1"));
    }

    @Test
    void ensureGetByIdThrows404WhenMissing() {
        when(scheduledFlightRepository.findById("missing")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.getById("missing"));
    }
}
