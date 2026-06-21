package isep.psoft.aisafe.flightroutes.services;

import isep.psoft.aisafe.aircraftmanagement.domain.Aircraft;
import isep.psoft.aisafe.aircraftmanagement.domain.RegistrationNumber;
import isep.psoft.aisafe.aircraftmanagement.repositories.AircraftRepository;
import isep.psoft.aisafe.airports.domain.AirportState;
import isep.psoft.aisafe.flightroutes.domain.FlightRoute;
import isep.psoft.aisafe.flightroutes.domain.FlightSchedule;
import isep.psoft.aisafe.flightroutes.domain.RouteRequirementsNotMetException;
import isep.psoft.aisafe.flightroutes.domain.ScheduledFlight;
import isep.psoft.aisafe.flightroutes.dto.CreateScheduledFlightDTO;
import isep.psoft.aisafe.flightroutes.factories.ScheduledFlightFactory;
import isep.psoft.aisafe.flightroutes.repositories.FlightRouteRepository;
import isep.psoft.aisafe.flightroutes.repositories.ScheduledFlightRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * US212 - Creates a scheduled flight, enforcing the business rules:
 * range/capacity compliance (422), aircraft availability, airport availability and
 * non-overlapping schedule (409). Missing aircraft/route -> 404.
 */
@Service
@RequiredArgsConstructor
public class CreateScheduledFlightService {

    private final AircraftRepository aircraftRepository;
    private final FlightRouteRepository flightRouteRepository;
    private final ScheduledFlightRepository scheduledFlightRepository;
    private final ScheduledFlightFactory factory;

    @Transactional
    public ScheduledFlight create(CreateScheduledFlightDTO dto) {
        // 404 - aircraft must exist
        Aircraft aircraft = aircraftRepository.findByRegistration_Registration(dto.getAircraftRegistration())
                .orElseThrow(() -> new EntityNotFoundException("Aircraft not found: " + dto.getAircraftRegistration()));

        // 404 - route must exist
        FlightRoute route = flightRouteRepository.findById(dto.getRouteID())
                .orElseThrow(() -> new EntityNotFoundException("Route not found: " + dto.getRouteID()));

        // 422 - aircraft must comply with the route requirements (range & capacity)
        double range = aircraft.getModel().getSpecifications().getMaximumRange();
        int capacity = aircraft.getSeatingCapacity().getTotalSeats();
        if (!route.getRequirements().isMetBy(range, capacity)) {
            throw new RouteRequirementsNotMetException(
                    "Aircraft " + dto.getAircraftRegistration()
                            + " does not meet the route requirements (range/capacity).");
        }

        // 409 - aircraft must be available
        if (!"ACTIVE".equals(aircraft.getStatus().getState())) {
            throw new IllegalStateException("Aircraft " + dto.getAircraftRegistration()
                    + " is not available (status: " + aircraft.getStatus().getState() + ").");
        }

        // 409 - origin and destination airports must be operational
        if (route.getOrigin().getStatus() != AirportState.OPERATIONAL) {
            throw new IllegalStateException("Origin airport "
                    + route.getOrigin().getIataCode().getCode() + " is not operational.");
        }
        if (route.getDestination().getStatus() != AirportState.OPERATIONAL) {
            throw new IllegalStateException("Destination airport "
                    + route.getDestination().getIataCode().getCode() + " is not operational.");
        }

        // 409 - the aircraft must not already have a flight at the same date/time
        if (scheduledFlightRepository.existsOverlappingFlight(
                dto.getAircraftRegistration(), dto.getDate(), dto.getTime())) {
            throw new IllegalStateException("Aircraft " + dto.getAircraftRegistration()
                    + " already has a scheduled flight at " + dto.getDate() + " " + dto.getTime() + ".");
        }

        FlightSchedule schedule = new FlightSchedule(dto.getDate(), dto.getTime());
        ScheduledFlight flight = factory.create(
                aircraft.getRegistrationNumber().getNumber(), route, schedule);
        return scheduledFlightRepository.save(flight);
    }
}
