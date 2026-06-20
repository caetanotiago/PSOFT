package isep.psoft.aisafe.airports.services;

import isep.psoft.aisafe.airports.domain.*;
import isep.psoft.aisafe.airports.repositories.AirportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroupAirportsUseCaseImplTest {

    @Mock
    private AirportRepository airportRepository;

    @InjectMocks
    private GroupAirportsUseCaseImpl useCase;

    private Airport lisbon;
    private Airport newYork;
    private Airport noRegion;

    @BeforeEach
    void setUp() {
        lisbon = new Airport(new IATACode("LIS"),
                new AirportDetails("Humberto Delgado", "Lisbon", "Portugal", "Europe", "Europe/Lisbon",
                        new Coordinates(38.77, -9.13)),
                AirportState.OPERATIONAL, List.of(new Runway("03/21", 3805.0, "030/210")));
        newYork = new Airport(new IATACode("JFK"),
                new AirportDetails("John F. Kennedy", "New York", "USA", "North America", "America/New_York",
                        new Coordinates(40.64, -73.78)),
                AirportState.OPERATIONAL, List.of(new Runway("04L/22R", 3682.0, "040/220")));
        noRegion = new Airport(new IATACode("XXX"),
                new AirportDetails("Unknown", "Nowhere", "Noland", null, "UTC",
                        new Coordinates(0.0, 0.0)),
                AirportState.OPERATIONAL, List.of(new Runway("09/27", 2000.0, "090/270")));
    }

    @Test
    void groups_airports_by_region() {
        when(airportRepository.findAll()).thenReturn(List.of(lisbon, newYork));

        List<AirportGroup> groups = useCase.groupBy("region");

        assertEquals(2, groups.size());
        assertTrue(groups.stream().anyMatch(g -> g.getGroupKey().equals("Europe")));
        assertTrue(groups.stream().anyMatch(g -> g.getGroupKey().equals("North America")));
    }

    @Test
    void groups_airports_by_country() {
        when(airportRepository.findAll()).thenReturn(List.of(lisbon, newYork));

        List<AirportGroup> groups = useCase.groupBy("country");

        assertTrue(groups.stream().anyMatch(g -> g.getGroupKey().equals("Portugal")));
        assertTrue(groups.stream().anyMatch(g -> g.getGroupKey().equals("USA")));
    }

    @Test
    void airports_with_blank_region_go_to_unspecified_group() {
        when(airportRepository.findAll()).thenReturn(List.of(noRegion));

        List<AirportGroup> groups = useCase.groupBy("region");

        assertEquals(1, groups.size());
        assertEquals("Unspecified", groups.get(0).getGroupKey());
    }

    @Test
    void groups_are_sorted_alphabetically_by_key() {
        when(airportRepository.findAll()).thenReturn(List.of(lisbon, newYork));

        List<AirportGroup> groups = useCase.groupBy("region");

        assertEquals("Europe", groups.get(0).getGroupKey());
        assertEquals("North America", groups.get(1).getGroupKey());
    }

    @Test
    void throws_illegal_argument_for_invalid_by_value() {
        assertThrows(IllegalArgumentException.class, () -> useCase.groupBy("city"));
        verify(airportRepository, never()).findAll();
    }

    @Test
    void throws_illegal_argument_for_null_by_value() {
        assertThrows(IllegalArgumentException.class, () -> useCase.groupBy(null));
    }
}
