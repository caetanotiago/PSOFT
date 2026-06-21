package isep.psoft.aisafe.flightroutes.services;

import isep.psoft.aisafe.aircraftmanagement.domain.Aircraft;
import isep.psoft.aisafe.aircraftmanagement.repositories.AircraftRepository;
import isep.psoft.aisafe.airports.domain.Airport;
import isep.psoft.aisafe.airports.domain.AirportState;
import isep.psoft.aisafe.airports.domain.IATACode;
import isep.psoft.aisafe.flightroutes.domain.FlightRoute;
import isep.psoft.aisafe.flightroutes.domain.RouteRequirements;
import isep.psoft.aisafe.flightroutes.domain.RouteRequirementsNotMetException;
import isep.psoft.aisafe.flightroutes.domain.ScheduledFlight;
import isep.psoft.aisafe.flightroutes.dto.CreateScheduledFlightDTO;
import isep.psoft.aisafe.flightroutes.factories.ScheduledFlightFactory;
import isep.psoft.aisafe.flightroutes.repositories.FlightRouteRepository;
import isep.psoft.aisafe.flightroutes.repositories.ScheduledFlightRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT) // setup partilhado; nem todos os stubs são usados por caso
class CreateScheduledFlightServiceTest {

    @Mock private AircraftRepository aircraftRepository;
    @Mock private FlightRouteRepository flightRouteRepository;
    @Mock private ScheduledFlightRepository scheduledFlightRepository;
    @Mock private ScheduledFlightFactory factory;

    @InjectMocks private CreateScheduledFlightService service;

    private static final String REG = "CS-TVA";
    private static final String ROUTE_ID = "route-1";

    private Aircraft aircraft;
    private FlightRoute route;
    private Airport origin;
    private Airport destination;
    private RouteRequirements requirements;
    private CreateScheduledFlightDTO dto;

    @BeforeEach
    void setUp() {
        // Aircraft: deep stubs (sem enums na cadeia).
        aircraft = mock(Aircraft.class, RETURNS_DEEP_STUBS);
        // Route/Airport: mocks shallow — getStatus() devolve o enum AirportState (não mockável).
        route = mock(FlightRoute.class);
        origin = mock(Airport.class);
        destination = mock(Airport.class);
        requirements = mock(RouteRequirements.class);

        dto = new CreateScheduledFlightDTO();
        dto.setAircraftRegistration(REG);
        dto.setRouteID(ROUTE_ID);
        dto.setDate(LocalDate.of(2026, 7, 1));
        dto.setTime(LocalTime.of(10, 0));

        // Cenário "tudo válido" por omissão; cada teste sobrepõe o que precisa.
        when(aircraftRepository.findByRegistration_Registration(REG)).thenReturn(Optional.of(aircraft));
        when(flightRouteRepository.findById(ROUTE_ID)).thenReturn(Optional.of(route));

        when(aircraft.getModel().getSpecifications().getMaximumRange()).thenReturn(6000.0);
        when(aircraft.getSeatingCapacity().getTotalSeats()).thenReturn(180);
        when(aircraft.getStatus().getState()).thenReturn("AVAILABLE");
        when(aircraft.getRegistrationNumber().getNumber()).thenReturn(REG);

        when(route.getRequirements()).thenReturn(requirements);
        when(requirements.isMetBy(anyDouble(), anyInt())).thenReturn(true);
        when(route.getOrigin()).thenReturn(origin);
        when(route.getDestination()).thenReturn(destination);
        when(origin.getStatus()).thenReturn(AirportState.OPERATIONAL);
        when(destination.getStatus()).thenReturn(AirportState.OPERATIONAL);
        when(origin.getIataCode()).thenReturn(new IATACode("LIS"));
        when(destination.getIataCode()).thenReturn(new IATACode("MAD"));

        when(scheduledFlightRepository.existsOverlappingFlight(eq(REG), any(), any())).thenReturn(false);
    }

    @Test
    void ensureHappyPathCreatesAndSavesFlight() {
        ScheduledFlight created = mock(ScheduledFlight.class);
        when(factory.create(eq(REG), eq(route), any())).thenReturn(created);
        when(scheduledFlightRepository.save(created)).thenReturn(created);

        ScheduledFlight result = service.create(dto);

        assertSame(created, result);
        verify(factory).create(eq(REG), eq(route), any());
        verify(scheduledFlightRepository).save(created);
    }

    @Test
    void ensureAircraftNotFoundThrows404() {
        when(aircraftRepository.findByRegistration_Registration(REG)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.create(dto));
        verify(scheduledFlightRepository, never()).save(any());
    }

    @Test
    void ensureRouteNotFoundThrows404() {
        when(flightRouteRepository.findById(ROUTE_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.create(dto));
        verify(scheduledFlightRepository, never()).save(any());
    }

    @Test
    void ensureRequirementsNotMetThrows422() {
        when(requirements.isMetBy(anyDouble(), anyInt())).thenReturn(false);

        assertThrows(RouteRequirementsNotMetException.class, () -> service.create(dto));
        verify(scheduledFlightRepository, never()).save(any());
    }

    @Test
    void ensureAircraftNotAvailableThrows409() {
        when(aircraft.getStatus().getState()).thenReturn("UNDER_MAINTENANCE");

        assertThrows(IllegalStateException.class, () -> service.create(dto));
        verify(scheduledFlightRepository, never()).save(any());
    }

    @Test
    void ensureOriginAirportNotOperationalThrows409() {
        when(origin.getStatus()).thenReturn(AirportState.CLOSED);

        assertThrows(IllegalStateException.class, () -> service.create(dto));
        verify(scheduledFlightRepository, never()).save(any());
    }

    @Test
    void ensureDestinationAirportNotOperationalThrows409() {
        when(destination.getStatus()).thenReturn(AirportState.UNDER_MAINTENANCE);

        assertThrows(IllegalStateException.class, () -> service.create(dto));
        verify(scheduledFlightRepository, never()).save(any());
    }

    @Test
    void ensureOverlappingFlightThrows409() {
        when(scheduledFlightRepository.existsOverlappingFlight(eq(REG), any(), any())).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> service.create(dto));
        verify(scheduledFlightRepository, never()).save(any());
    }
}
