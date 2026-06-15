package isep.psoft.aisafe.flightroutes.services;

import isep.psoft.aisafe.aircraftmanagement.domain.RegistrationNumber;
import isep.psoft.aisafe.aircraftmanagement.repositories.AircraftRepository;
import isep.psoft.aisafe.flightroutes.domain.ScheduledFlight;
import isep.psoft.aisafe.flightroutes.repositories.ScheduledFlightRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * US213 - Read access to scheduled flights (per aircraft, paginated, and by id).
 */
@Service
@RequiredArgsConstructor
public class ViewScheduledFlightsByAircraftService {

    private final ScheduledFlightRepository scheduledFlightRepository;
    private final AircraftRepository aircraftRepository;

    @Transactional(readOnly = true)
    public Page<ScheduledFlight> viewByAircraft(String registration, Pageable pageable) {
        // 404 - aircraft must exist
        if (!aircraftRepository.existsByRegistrationNumber(new RegistrationNumber(registration))) {
            throw new EntityNotFoundException("Aircraft not found: " + registration);
        }
        return scheduledFlightRepository.findByAircraftRegistration(registration, pageable);
    }

    @Transactional(readOnly = true)
    public ScheduledFlight getById(String id) {
        return scheduledFlightRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Scheduled flight not found: " + id));
    }
}
