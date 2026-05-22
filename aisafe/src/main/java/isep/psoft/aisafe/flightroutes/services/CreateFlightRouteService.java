package isep.psoft.aisafe.flightroutes.services;

import isep.psoft.aisafe.airports.domain.Airport;
import isep.psoft.aisafe.airports.repositories.AirportRepository; 
import isep.psoft.aisafe.flightroutes.domain.FlightRoute;
import isep.psoft.aisafe.flightroutes.dto.CreateRouteDTO;
import isep.psoft.aisafe.flightroutes.factories.FlightRouteFactory;
import isep.psoft.aisafe.flightroutes.repositories.FlightRouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateFlightRouteService {

    private final AirportRepository airportRepository;
    private final FlightRouteRepository routeRepository;
    private final FlightRouteFactory routeFactory;
    private final DistanceCalculatorService distanceCalculator;

    @Transactional
    public FlightRoute createRoute(CreateRouteDTO dto) {
        
        // Validar se a rota já existe 
        if (routeRepository.existsByOriginAndDestination(dto.getOriginIATA(), dto.getDestIATA())) {
            throw new IllegalArgumentException("A route between these airports already exists.");
        }

        // Procurar Aeroportos (lança exceção se não existirem)
        Airport origin = airportRepository.findById(dto.getOriginIATA())
                .orElseThrow(() -> new IllegalArgumentException("Origin airport not found: " + dto.getOriginIATA()));
        
        Airport dest = airportRepository.findById(dto.getDestIATA())
                .orElseThrow(() -> new IllegalArgumentException("Destination airport not found: " + dto.getDestIATA()));

        // Calcular Distância
        double distance = distanceCalculator.calculateDistance(
                origin.getLatitude(), origin.getLongitude(),
                dest.getLatitude(), dest.getLongitude()
        );

        // Criar Entidade usando a Factory
        FlightRoute newRoute = routeFactory.createRoute(
                origin, dest, distance, 
                dto.getMinRange(), dto.getMinCapacity(), dto.getEstimatedFlightTime()
        );

        // Persistir no Repositório
        return routeRepository.save(newRoute);
    }
}