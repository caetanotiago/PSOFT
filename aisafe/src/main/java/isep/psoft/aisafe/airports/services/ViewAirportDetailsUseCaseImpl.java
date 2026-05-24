package isep.psoft.aisafe.airports.services;

import isep.psoft.aisafe.airports.domain.Airport;
import isep.psoft.aisafe.airports.domain.AirportNotFoundException;
import isep.psoft.aisafe.airports.domain.IATACode;
import isep.psoft.aisafe.airports.repositories.AirportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ViewAirportDetailsUseCaseImpl implements ViewAirportDetailsUseCase {

    private final AirportRepository airportRepository;

    public ViewAirportDetailsUseCaseImpl(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    @Override
    public Airport getAirportByIataCode(String iataCode) {
        IATACode key = new IATACode(iataCode); // valida formato; lança IllegalArgumentException se inválido
        return airportRepository.findById(key)
                .orElseThrow(() -> new AirportNotFoundException(iataCode));
    }
}
