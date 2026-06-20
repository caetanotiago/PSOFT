package isep.psoft.aisafe.airports.services;

import isep.psoft.aisafe.airports.domain.Airport;
import isep.psoft.aisafe.airports.domain.AirportRouteCount;
import isep.psoft.aisafe.airports.repositories.AirportRepository;
import isep.psoft.aisafe.flightroutes.repositories.FlightRouteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// US210 - Estatísticas dos aeroportos mais movimentados por número de rotas.
// routeCount(airport) = nº de rotas em que o aeroporto é origem + nº em que é destino.
// Nunca persistido; recalculado em cada pedido, tal como RouteUsage (US214).
@Service
public class ListBusiestAirportsUseCaseImpl implements ListBusiestAirportsUseCase {

    private final AirportRepository airportRepository;
    private final FlightRouteRepository flightRouteRepository;

    public ListBusiestAirportsUseCaseImpl(AirportRepository airportRepository,
                                          FlightRouteRepository flightRouteRepository) {
        this.airportRepository = airportRepository;
        this.flightRouteRepository = flightRouteRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AirportRouteCount> listBusiest(Integer limit) {
        if (limit != null && limit <= 0)
            throw new IllegalArgumentException("limit must be a positive integer");

        Map<String, Long> counts = new HashMap<>();
        for (Object[] row : flightRouteRepository.countByOrigin()) {
            counts.merge((String) row[0], (Long) row[1], Long::sum);
        }
        for (Object[] row : flightRouteRepository.countByDestination()) {
            counts.merge((String) row[0], (Long) row[1], Long::sum);
        }

        List<Airport> airports = airportRepository.findAll();

        List<AirportRouteCount> result = airports.stream()
                .map(a -> new AirportRouteCount(a, counts.getOrDefault(a.getIataCode().getCode(), 0L)))
                .sorted(Comparator.comparingLong(AirportRouteCount::getRouteCount).reversed())
                .toList();

        return limit == null ? result : result.stream().limit(limit).toList();
    }
}
