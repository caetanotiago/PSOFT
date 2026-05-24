package isep.psoft.aisafe.airports.services;

import isep.psoft.aisafe.airports.domain.*;
import isep.psoft.aisafe.airports.repositories.AirportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RegisterAirportUseCaseImpl implements RegisterAirportUseCase {

    private final AirportRepository airportRepository;

    public RegisterAirportUseCaseImpl(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    @Override
    public Airport registerAirport(String iataCode, String name, String city, String country,
                                   String region, String timezone,
                                   Double latitude, Double longitude,
                                   List<Runway> runways) {
        IATACode code = new IATACode(iataCode.toUpperCase());

        if (airportRepository.existsById(code))
            throw new DuplicateIATACodeException(code.getCode());

        AirportDetails details = new AirportDetails(
                name, city, country, region, timezone,
                new Coordinates(latitude, longitude)
        );

        Airport airport = new Airport(code, details, AirportState.OPERATIONAL, runways);
        return airportRepository.save(airport);
    }
}
