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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddPhotoUseCaseImplTest {

    @Mock
    private AirportRepository airportRepository;

    @InjectMocks
    private AddPhotoUseCaseImpl useCase;

    private Airport lisbon;

    @BeforeEach
    void setUp() {
        lisbon = new Airport(
                new IATACode("LIS"),
                new AirportDetails("Humberto Delgado", "Lisbon", "Portugal", "Europe",
                        "Europe/Lisbon", new Coordinates(38.77, -9.13)),
                AirportState.OPERATIONAL,
                List.of(new Runway("03/21", 3805.0, "030/210")));
    }

    @Test
    void adds_photo_successfully() {
        when(airportRepository.findById(new IATACode("LIS"))).thenReturn(Optional.of(lisbon));
        when(airportRepository.save(any(Airport.class))).thenAnswer(i -> i.getArgument(0));

        Airport result = useCase.addPhoto("LIS", "https://example.com/lis.jpg", "Terminal view");

        assertEquals(1, result.getPhotos().size());
        verify(airportRepository).save(lisbon);
    }

    @Test
    void throws_not_found_when_airport_missing() {
        when(airportRepository.findById(new IATACode("XXX"))).thenReturn(Optional.empty());

        assertThrows(AirportNotFoundException.class, () ->
                useCase.addPhoto("XXX", "https://example.com/x.jpg", null));
        verify(airportRepository, never()).save(any());
    }
}
