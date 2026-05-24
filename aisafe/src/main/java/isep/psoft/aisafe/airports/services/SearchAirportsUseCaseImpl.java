package isep.psoft.aisafe.airports.services;

import isep.psoft.aisafe.airports.domain.Airport;
import isep.psoft.aisafe.airports.repositories.AirportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class SearchAirportsUseCaseImpl implements SearchAirportsUseCase {

    private final AirportRepository airportRepository;

    public SearchAirportsUseCaseImpl(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    @Override
    public List<Airport> searchAirports(String name, String city, String country) {
        return airportRepository.searchByCriteria(name, city, country);
    }
}
