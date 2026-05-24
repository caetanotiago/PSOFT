package isep.psoft.aisafe.aircraftmanagement.services;

import isep.psoft.aisafe.aircraftmanagement.domain.Aircraft;
import isep.psoft.aisafe.aircraftmanagement.domain.AircraftStatus;
import isep.psoft.aisafe.aircraftmanagement.domain.RegistrationNumber;
import isep.psoft.aisafe.aircraftmanagement.repositories.AircraftRepository;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// CORREÇÃO: Removido o import inválido de 'isep.psoft.aisafe.exceptions'

@Service
public class UpdateAircraftStatusServiceImpl implements UpdateAircraftStatusService {

    private final AircraftRepository aircraftRepository;

    public UpdateAircraftStatusServiceImpl(AircraftRepository aircraftRepository) {
        this.aircraftRepository = aircraftRepository;
    }

    @Override
    @Transactional
    public Aircraft updateStatus(String registrationNumber, String newStatus, Long version) {
        RegistrationNumber regNum = new RegistrationNumber(registrationNumber);

        // CORREÇÃO: Substituída a NotFoundException (que não existe) por uma RuntimeException padrão.
        Aircraft aircraft = aircraftRepository.findById(regNum)
                .orElseThrow(() -> new RuntimeException("Aircraft with registration number '" + registrationNumber + "' not found."));

        // Validação manual de Optimistic Locking. Garante que o utilizador está a trabalhar com dados atualizados.
        if (version == null || !aircraft.getVersion().equals(version)) {
            throw new ObjectOptimisticLockingFailureException(Aircraft.class, aircraft.getRegistrationNumber());
        }

        // Validação do estado acontece dentro do Value Object AircraftStatus
        aircraft.updateStatus(new AircraftStatus(newStatus));
        return aircraftRepository.save(aircraft);
    }
}
