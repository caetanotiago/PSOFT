package isep.psoft.aisafe.airports.factories;

import isep.psoft.aisafe.airports.domain.*;
import isep.psoft.aisafe.airports.dto.RunwayRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AirportFactory {

    public Airport create(String iataCode, String name, String city, String country,
                          String region, String timezone,
                          Double latitude, Double longitude,
                          List<RunwayRequest> runwayRequests) {

        IATACode code = new IATACode(iataCode.toUpperCase());

        AirportDetails details = new AirportDetails(
                name, city, country, region, timezone,
                new Coordinates(latitude, longitude)
        );

        List<Runway> runways = runwayRequests.stream()
                .map(r -> new Runway(r.name(), r.length(), r.orientation()))
                .toList();

        return new Airport(code, details, AirportState.OPERATIONAL, runways);
    }
}
