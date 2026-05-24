package isep.psoft.aisafe.airports.services;

import isep.psoft.aisafe.airports.domain.Airport;
import isep.psoft.aisafe.airports.domain.AirportNotFoundException;
import isep.psoft.aisafe.airports.domain.IATACode;
import isep.psoft.aisafe.airports.repositories.AirportRepository;
import isep.psoft.aisafe.domain.shared.ModelDesignation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AddCertificationUseCaseImpl implements AddCertificationUseCase {

    private final AirportRepository airportRepository;

    public AddCertificationUseCaseImpl(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    @Override
    public Airport addCertification(String iataCode, String manufacturer, String modelName) {
        IATACode key = new IATACode(iataCode.toUpperCase());
        Airport airport = airportRepository.findById(key)
                .orElseThrow(() -> new AirportNotFoundException(iataCode));

        airport.addCertification(new ModelDesignation(manufacturer, modelName));
        return airportRepository.save(airport);
    }
}
