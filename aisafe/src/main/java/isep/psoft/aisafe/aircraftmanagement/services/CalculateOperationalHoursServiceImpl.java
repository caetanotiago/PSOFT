package isep.psoft.aisafe.aircraftmanagement.services;

import isep.psoft.aisafe.aircraftmanagement.dto.AircraftOperationalHoursDTO;
import isep.psoft.aisafe.aircraftmanagement.repositories.AircraftRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

// A anotação @Service é OBRIGATÓRIA para o Spring detetar e resolver o teu erro de arranque!
@Service
public class CalculateOperationalHoursServiceImpl implements CalculateOperationalHoursService {

    private final AircraftRepository aircraftRepository;

    public CalculateOperationalHoursServiceImpl(AircraftRepository aircraftRepository) {
        this.aircraftRepository = aircraftRepository;
    }

    @Override
    public Page<AircraftOperationalHoursDTO> calculateOperationalHours(Pageable pageable) {
        // Delegamos o trabalho pesado de agregação (SUM) e paginação diretamente para a Base de Dados
        return aircraftRepository.findOperationalHoursPerAircraft(pageable);
    }
}