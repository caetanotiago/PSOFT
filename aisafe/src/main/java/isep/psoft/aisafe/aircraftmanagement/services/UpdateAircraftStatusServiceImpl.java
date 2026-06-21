package isep.psoft.aisafe.aircraftmanagement.services;

import isep.psoft.aisafe.aircraftmanagement.domain.Aircraft;
import isep.psoft.aisafe.aircraftmanagement.domain.AircraftNotFoundException;
import isep.psoft.aisafe.aircraftmanagement.domain.AircraftStatus;
import isep.psoft.aisafe.aircraftmanagement.domain.RegistrationNumber;
import isep.psoft.aisafe.aircraftmanagement.repositories.AircraftRepository;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        // CORRIGIDO: era "new RuntimeException(...)" — agora usa a exceção de domínio
        // já existente no projeto, que o GlobalExceptionHandler mapeia para 404.
        Aircraft aircraft = aircraftRepository.findById(regNum)
                .orElseThrow(() -> new AircraftNotFoundException(registrationNumber));

        // Validação manual de Optimistic Locking. Garante que o utilizador está
        // a trabalhar com dados atualizados. Mapeada para 409 pelo GlobalExceptionHandler.
        if (version == null || !aircraft.getVersion().equals(version)) {
            throw new ObjectOptimisticLockingFailureException(Aircraft.class, aircraft.getRegistrationNumber());
        }

        // Validação do estado acontece dentro do Value Object AircraftStatus
        aircraft.updateStatus(new AircraftStatus(newStatus));
        return aircraftRepository.save(aircraft);
    }
}