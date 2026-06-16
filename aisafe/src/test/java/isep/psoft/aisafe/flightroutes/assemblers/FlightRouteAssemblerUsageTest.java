package isep.psoft.aisafe.flightroutes.assemblers;

import isep.psoft.aisafe.flightroutes.domain.FlightRoute;
import isep.psoft.aisafe.flightroutes.domain.RouteUsage;
import isep.psoft.aisafe.flightroutes.dto.FlightRouteDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Cobre a parte WP#3B (US214) do FlightRouteAssembler: mapeamento da popularidade (usageCount).
 */
class FlightRouteAssemblerUsageTest {

    private final FlightRouteAssembler assembler = new FlightRouteAssembler();

    private FlightRoute route(String id, double distance) {
        FlightRoute r = mock(FlightRoute.class, RETURNS_DEEP_STUBS);
        when(r.getId()).thenReturn(id);
        when(r.getOrigin().getIataCode().getCode()).thenReturn("LIS");
        when(r.getDestination().getIataCode().getCode()).thenReturn("OPO");
        when(r.getDistance().getDistance()).thenReturn(distance);
        when(r.getRequirements().getMinRange()).thenReturn(1000.0);
        when(r.getRequirements().getMinCapacity()).thenReturn(100);
        when(r.getEstimatedFlightTime().getDurationMinutes()).thenReturn(60);
        when(r.getStatus().getState()).thenReturn("ACTIVE");
        return r;
    }

    @Test
    void ensureToDTOWithUsageSetsUsageCount() {
        FlightRouteDTO dto = assembler.toDTOWithUsage(new RouteUsage(route("r1", 300.0), 5L));

        assertEquals("r1", dto.getId());
        assertEquals(5L, dto.getUsageCount());
    }

    @Test
    void ensureToDTOWithUsageListMapsAll() {
        List<FlightRouteDTO> dtos = assembler.toDTOWithUsageList(List.of(
                new RouteUsage(route("r1", 300.0), 5L),
                new RouteUsage(route("r2", 900.0), 0L)));

        assertEquals(2, dtos.size());
        assertEquals(5L, dtos.get(0).getUsageCount());
        assertEquals(0L, dtos.get(1).getUsageCount());
    }
}
