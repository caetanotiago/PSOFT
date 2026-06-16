package isep.psoft.aisafe.flightroutes.assemblers;

import isep.psoft.aisafe.flightroutes.domain.FlightRoute;
import isep.psoft.aisafe.flightroutes.domain.Itinerary;
import isep.psoft.aisafe.flightroutes.dto.ItineraryDTO;
import isep.psoft.aisafe.flightroutes.dto.RouteLegDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ItineraryAssemblerTest {

    private final ItineraryAssembler assembler = new ItineraryAssembler();

    private FlightRoute leg(String id, String origin, String dest, double km) {
        FlightRoute r = mock(FlightRoute.class, RETURNS_DEEP_STUBS);
        when(r.getId()).thenReturn(id);
        when(r.getOrigin().getIataCode().getCode()).thenReturn(origin);
        when(r.getDestination().getIataCode().getCode()).thenReturn(dest);
        when(r.getDistance().getDistance()).thenReturn(km);
        return r;
    }

    @Test
    void ensureMapsLegsStopsAndTotalDistance() {
        Itinerary itinerary = new Itinerary(List.of(
                leg("r1", "LIS", "OPO", 300.0),
                leg("r2", "OPO", "MAD", 434.0)));

        ItineraryDTO dto = assembler.toDTO(itinerary);

        assertEquals(1, dto.getNumberOfStops());
        assertEquals(734.0, dto.getTotalDistance());
        assertEquals(2, dto.getLegs().size());
        RouteLegDTO first = dto.getLegs().get(0);
        assertEquals("r1", first.getRouteID());
        assertEquals("LIS", first.getOriginIATA());
        assertEquals("OPO", first.getDestIATA());
        assertEquals(300.0, first.getDistance());
    }

    @Test
    void ensureToDTOListMapsAll() {
        Itinerary a = new Itinerary(List.of(leg("r1", "LIS", "MAD", 500.0)));
        Itinerary b = new Itinerary(List.of(leg("r2", "LIS", "OPO", 300.0)));

        List<ItineraryDTO> dtos = assembler.toDTOList(List.of(a, b));

        assertEquals(2, dtos.size());
    }
}
