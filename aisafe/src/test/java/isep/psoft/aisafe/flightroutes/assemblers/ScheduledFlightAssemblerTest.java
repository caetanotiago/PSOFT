package isep.psoft.aisafe.flightroutes.assemblers;

import isep.psoft.aisafe.flightroutes.domain.ScheduledFlight;
import isep.psoft.aisafe.flightroutes.dto.ScheduledFlightDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ScheduledFlightAssemblerTest {

    private final ScheduledFlightAssembler assembler = new ScheduledFlightAssembler();

    @BeforeEach
    void setUpRequestContext() {
        // linkTo(methodOn(...)) precisa de um contexto de request ativo.
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));
    }

    @AfterEach
    void clearRequestContext() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void ensureMapsAllFieldsAndAddsLinks() {
        ScheduledFlight flight = mock(ScheduledFlight.class, RETURNS_DEEP_STUBS);
        when(flight.getId()).thenReturn("sf-1");
        when(flight.getAircraftRegistration()).thenReturn("CS-TVA");
        when(flight.getRoute().getId()).thenReturn("route-1");
        when(flight.getRoute().getOrigin().getIataCode().getCode()).thenReturn("LIS");
        when(flight.getRoute().getDestination().getIataCode().getCode()).thenReturn("OPO");
        when(flight.getSchedule().getDate()).thenReturn(LocalDate.of(2026, 7, 1));
        when(flight.getSchedule().getTime()).thenReturn(LocalTime.of(10, 0));
        when(flight.getStatus().getState()).thenReturn("SCHEDULED");

        ScheduledFlightDTO dto = assembler.toModel(flight);

        assertEquals("sf-1", dto.getId());
        assertEquals("CS-TVA", dto.getAircraftRegistration());
        assertEquals("route-1", dto.getRouteID());
        assertEquals("LIS", dto.getOriginIATA());
        assertEquals("OPO", dto.getDestIATA());
        assertEquals(LocalDate.of(2026, 7, 1), dto.getDate());
        assertEquals(LocalTime.of(10, 0), dto.getTime());
        assertEquals("SCHEDULED", dto.getStatus());
        assertTrue(dto.hasLink("self"));
        assertTrue(dto.hasLink("route"));
    }
}
