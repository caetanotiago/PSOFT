package isep.psoft.aisafe.airports.services;

import isep.psoft.aisafe.airports.domain.Airport;
import isep.psoft.aisafe.airports.dto.RunwayRequest;

import java.util.List;

public interface RegisterAirportUseCase {

    Airport registerAirport(String iataCode, String name, String city, String country,
                            String region, String timezone,
                            Double latitude, Double longitude,
                            List<RunwayRequest> runways);
}
