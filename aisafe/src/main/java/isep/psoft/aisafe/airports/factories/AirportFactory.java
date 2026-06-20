package isep.psoft.aisafe.airports.factories;

import isep.psoft.aisafe.airports.domain.*;
import isep.psoft.aisafe.airports.dto.FacilityRequest;
import isep.psoft.aisafe.airports.dto.PhotoRequest;
import isep.psoft.aisafe.airports.dto.RunwayRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AirportFactory {

    public Airport create(String iataCode, String name, String city, String country,
                          String region, String timezone,
                          Double latitude, Double longitude,
                          List<RunwayRequest> runwayRequests,
                          List<FacilityRequest> facilityRequests,
                          List<PhotoRequest> photoRequests) {

        IATACode code = new IATACode(iataCode.toUpperCase());

        AirportDetails details = new AirportDetails(
                name, city, country, region, timezone,
                new Coordinates(latitude, longitude)
        );

        List<Runway> runways = runwayRequests.stream()
                .map(r -> new Runway(r.name(), r.length(), r.orientation()))
                .toList();

        Airport airport = new Airport(code, details, AirportState.OPERATIONAL, runways);

        // US207: optional facilities/photos supplied at registration time.
        if (facilityRequests != null) {
            facilityRequests.forEach(f -> airport.addFacility(new Facility(f.type(), f.identifier(), f.description())));
        }
        if (photoRequests != null) {
            photoRequests.forEach(p -> airport.addPhoto(new Photo(p.url(), p.caption())));
        }

        return airport;
    }
}
