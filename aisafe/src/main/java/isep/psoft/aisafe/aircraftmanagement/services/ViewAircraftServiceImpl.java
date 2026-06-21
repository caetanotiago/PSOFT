package isep.psoft.aisafe.aircraftmanagement.services;

import isep.psoft.aisafe.aircraftmanagement.domain.Aircraft;
import isep.psoft.aisafe.aircraftmanagement.domain.AircraftNotFoundException;
import isep.psoft.aisafe.aircraftmanagement.domain.RegistrationNumber;
import isep.psoft.aisafe.aircraftmanagement.repositories.AircraftRepository;
import org.springframework.stereotype.Service;

@Service
public class ViewAircraftServiceImpl implements ViewAircraftService {

    private final AircraftRepository aircraftRepository;

    public ViewAircraftServiceImpl(AircraftRepository aircraftRepository) {
        this.aircraftRepository = aircraftRepository;
    }

    @Override
    public Aircraft getAircraftByRegistrationNumber(String registrationNumber) {
        RegistrationNumber regNum = new RegistrationNumber(registrationNumber);
        return aircraftRepository.findById(regNum)
                .orElseThrow(() -> new AircraftNotFoundException(registrationNumber));
    }
}