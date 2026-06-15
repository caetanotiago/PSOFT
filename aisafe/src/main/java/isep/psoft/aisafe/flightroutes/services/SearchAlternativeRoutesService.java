package isep.psoft.aisafe.flightroutes.services;

import isep.psoft.aisafe.airports.domain.IATACode;
import isep.psoft.aisafe.airports.repositories.AirportRepository;
import isep.psoft.aisafe.flightroutes.domain.Itinerary;
import isep.psoft.aisafe.flightroutes.repositories.FlightRouteRepository;
import isep.psoft.aisafe.flightroutes.services.strategy.RouteSearchStrategy;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * US216 - Searches for alternative routes (itineraries) between two airports using a
 * pluggable algorithm (Strategy Pattern; default = fewest stops).
 */
@Service
@RequiredArgsConstructor
public class SearchAlternativeRoutesService {

    private final FlightRouteRepository flightRouteRepository;
    private final AirportRepository airportRepository;
    private final RouteSearchStrategy routeSearchStrategy;

    @Transactional(readOnly = true)
    public List<Itinerary> searchAlternatives(String origin, String dest) {
        String originCode = origin == null ? null : origin.toUpperCase();
        String destCode = dest == null ? null : dest.toUpperCase();

        // 404 - origin and destination airports must exist
        if (originCode == null || !airportRepository.existsById(new IATACode(originCode))) {
            throw new EntityNotFoundException("Airport not found: " + origin);
        }
        if (destCode == null || !airportRepository.existsById(new IATACode(destCode))) {
            throw new EntityNotFoundException("Airport not found: " + dest);
        }

        return routeSearchStrategy.search(flightRouteRepository.findAllActive(), originCode, destCode);
    }
}
