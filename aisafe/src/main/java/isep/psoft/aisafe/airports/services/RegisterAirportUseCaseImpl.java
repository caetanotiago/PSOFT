package isep.psoft.aisafe.airports.services;

import isep.psoft.aisafe.airports.domain.Airport;
import isep.psoft.aisafe.airports.domain.DuplicateIATACodeException;
import isep.psoft.aisafe.airports.domain.IATACode;
import isep.psoft.aisafe.airports.dto.FacilityRequest;
import isep.psoft.aisafe.airports.dto.PhotoRequest;
import isep.psoft.aisafe.airports.dto.RunwayRequest;
import isep.psoft.aisafe.airports.factories.AirportFactory;
import isep.psoft.aisafe.airports.repositories.AirportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RegisterAirportUseCaseImpl implements RegisterAirportUseCase {

    private final AirportRepository airportRepository;
    private final AirportFactory airportFactory;

    public RegisterAirportUseCaseImpl(AirportRepository airportRepository, AirportFactory airportFactory) {
        this.airportRepository = airportRepository;
        this.airportFactory = airportFactory;
    }

    @Override
    public Airport registerAirport(String iataCode, String name, String city, String country,
                                   String region, String timezone,
                                   Double latitude, Double longitude,
                                   List<RunwayRequest> runways,
                                   List<FacilityRequest> facilities,
                                   List<PhotoRequest> photos) {
        IATACode code = new IATACode(iataCode.toUpperCase());

        if (airportRepository.existsById(code))
            throw new DuplicateIATACodeException(code.getCode());

        Airport airport = airportFactory.create(iataCode, name, city, country, region, timezone,
                latitude, longitude, runways, facilities, photos);

        return airportRepository.save(airport);
    }
}
