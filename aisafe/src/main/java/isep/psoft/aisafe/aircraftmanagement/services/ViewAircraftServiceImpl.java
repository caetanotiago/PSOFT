package isep.psoft.aisafe.aircraftmanagement.services;

import isep.psoft.aisafe.aircraftmanagement.domain.Aircraft;
import isep.psoft.aisafe.aircraftmanagement.domain.RegistrationNumber;
import isep.psoft.aisafe.aircraftmanagement.repositories.AircraftRepository;
import org.springframework.stereotype.Service;

// CORREÇÃO: Removido o import inválido de 'isep.psoft.aisafe.exceptions'

@Service
public class ViewAircraftServiceImpl implements ViewAircraftService {

    private final AircraftRepository aircraftRepository;

    public ViewAircraftServiceImpl(AircraftRepository aircraftRepository) {
        this.aircraftRepository = aircraftRepository;
    }

    @Override
    public Aircraft getAircraftByRegistrationNumber(String registrationNumber) {
        RegistrationNumber regNum = new RegistrationNumber(registrationNumber); // Valida o formato automaticamente
        
        // CORREÇÃO: Substituída a NotFoundException (que não existe) por uma RuntimeException padrão.
        return aircraftRepository.findById(regNum)
                .orElseThrow(() -> new RuntimeException("Aircraft with registration number '" + registrationNumber + "' not found."));
    }
}
