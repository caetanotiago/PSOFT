package isep.psoft.aisafe.airports.services;

import isep.psoft.aisafe.airports.domain.Airport;
import isep.psoft.aisafe.airports.domain.AirportNotFoundException;
import isep.psoft.aisafe.airports.domain.Facility;
import isep.psoft.aisafe.airports.domain.IATACode;
import isep.psoft.aisafe.airports.repositories.AirportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AddFacilityUseCaseImpl implements AddFacilityUseCase {

    private final AirportRepository airportRepository;

    public AddFacilityUseCaseImpl(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    @Override
    public Airport addFacility(String iataCode, String type, String identifier, String description) {
        IATACode key = new IATACode(iataCode.toUpperCase());
        Airport airport = airportRepository.findById(key)
                .orElseThrow(() -> new AirportNotFoundException(iataCode));

        airport.addFacility(new Facility(type, identifier, description));
        return airportRepository.save(airport);
    }
}
