package isep.psoft.aisafe.airports.services;

import isep.psoft.aisafe.airports.domain.Airport;
import isep.psoft.aisafe.airports.domain.AirportNotFoundException;
import isep.psoft.aisafe.airports.domain.AirportState;
import isep.psoft.aisafe.airports.domain.IATACode;
import isep.psoft.aisafe.airports.repositories.AirportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UpdateAirportStatusUseCaseImpl implements UpdateAirportStatusUseCase {

    private final AirportRepository airportRepository;

    public UpdateAirportStatusUseCaseImpl(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    @Override
    public Airport updateStatus(String iataCode, String newStateStr) {
        // Fail-fast: validate the state string before any DB access.
        // valueOf throws IllegalArgumentException → GlobalExceptionHandler → 400.
        AirportState newState = AirportState.valueOf(newStateStr.toUpperCase());

        IATACode key = new IATACode(iataCode.toUpperCase());
        Airport airport = airportRepository.findById(key)
                .orElseThrow(() -> new AirportNotFoundException(iataCode));

        // changeStatus validates the transition → InvalidStatusTransitionException → 409.
        airport.changeStatus(newState);

        // save triggers @Version check → ObjectOptimisticLockingFailureException → 409.
        return airportRepository.save(airport);
    }
}
