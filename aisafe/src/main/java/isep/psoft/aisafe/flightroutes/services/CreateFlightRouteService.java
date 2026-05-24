package isep.psoft.aisafe.flightroutes.services;

import isep.psoft.aisafe.airports.domain.Airport;
import isep.psoft.aisafe.airports.domain.IATACode;
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
        
        if (routeRepository.existsByOriginAndDestination(dto.getOriginIATA(), dto.getDestIATA())) {
            throw new IllegalArgumentException("A route between these airports already exists.");
        }

        IATACode originIATA = new IATACode(dto.getOriginIATA());
        IATACode destIATA = new IATACode(dto.getDestIATA());

        Airport origin = airportRepository.findById(originIATA)
                .orElseThrow(() -> new IllegalArgumentException("Origin airport not found: " + dto.getOriginIATA()));
        
        Airport dest = airportRepository.findById(destIATA)
                .orElseThrow(() -> new IllegalArgumentException("Destination airport not found: " + dto.getDestIATA()));

        // CORREÇÃO DEFINITIVA: A latitude e longitude estão dentro de details -> coordinates.
        double distance = distanceCalculator.calculateDistance(
                origin.getDetails().getCoordinates().getLatitude(), 
                origin.getDetails().getCoordinates().getLongitude(),
                dest.getDetails().getCoordinates().getLatitude(),
                dest.getDetails().getCoordinates().getLongitude()
        );

        FlightRoute newRoute = routeFactory.createRoute(
                origin, dest, distance, 
                dto.getMinRange(), dto.getMinCapacity(), dto.getEstimatedFlightTime()
        );

        return routeRepository.save(newRoute);
    }
}
