package isep.psoft.aisafe.flightroutes.services;

import isep.psoft.aisafe.airports.domain.AirportNotFoundException;
import isep.psoft.aisafe.airports.domain.IATACode;
import isep.psoft.aisafe.airports.repositories.AirportRepository;
import isep.psoft.aisafe.flightroutes.domain.FlightRoute;
import isep.psoft.aisafe.flightroutes.repositories.FlightRouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// US209 - Visualizar todas as rotas que partem de ou chegam a um aeroporto específico.
// Vive no módulo flightroutes (o recurso devolvido é FlightRoute); depende do
// AirportRepository apenas para validar a existência do aeroporto (mesma direção de
// dependência já usada por CreateFlightRouteService, US110).
@Service
@RequiredArgsConstructor
public class ViewRoutesByAirportService {

    private final AirportRepository airportRepository;
    private final FlightRouteRepository flightRouteRepository;

    @Transactional(readOnly = true)
    public List<FlightRoute> findRoutesByAirport(String iataCode) {
        String code = iataCode.toUpperCase();
        if (!airportRepository.existsById(new IATACode(code)))
            throw new AirportNotFoundException(iataCode);

        return flightRouteRepository.findByOriginOrDestination(code);
    }
}
