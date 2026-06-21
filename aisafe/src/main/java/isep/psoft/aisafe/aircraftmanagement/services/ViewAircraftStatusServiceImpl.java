package isep.psoft.aisafe.aircraftmanagement.services;

import isep.psoft.aisafe.aircraftmanagement.domain.Aircraft;
import isep.psoft.aisafe.aircraftmanagement.domain.AircraftNotFoundException;
import isep.psoft.aisafe.aircraftmanagement.domain.RegistrationNumber;
import isep.psoft.aisafe.aircraftmanagement.dto.AircraftStatusDTO;
import isep.psoft.aisafe.aircraftmanagement.repositories.AircraftRepository;
import org.springframework.stereotype.Service;

@Service
public class ViewAircraftStatusServiceImpl implements ViewAircraftStatusService {

    private final AircraftRepository aircraftRepository;

    public ViewAircraftStatusServiceImpl(AircraftRepository aircraftRepository) {
        this.aircraftRepository = aircraftRepository;
    }

    @Override
    public AircraftStatusDTO getAircraftStatus(String registrationNumber) {
        Aircraft aircraft = aircraftRepository.findByRegistration_Registration(registrationNumber)
                .orElseThrow(() -> new AircraftNotFoundException(registrationNumber));
        return new AircraftStatusDTO(
                aircraft.getRegistrationNumber().getNumber(),
                aircraft.getStatus().getState()
        );
    }
}