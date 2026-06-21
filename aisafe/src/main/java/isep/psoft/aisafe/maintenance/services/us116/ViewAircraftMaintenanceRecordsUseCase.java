package isep.psoft.aisafe.maintenance.services.us116;

import isep.psoft.aisafe.aircraftmanagement.repositories.AircraftRepository;
import isep.psoft.aisafe.maintenance.assemblers.MaintenanceRecordAssembler;
import isep.psoft.aisafe.maintenance.dto.MaintenanceRecordOutputDto;
import isep.psoft.aisafe.maintenance.repositories.MaintenanceRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service("ViewAircraftMaintenanceRecordsUseCase")
@RequiredArgsConstructor
public class ViewAircraftMaintenanceRecordsUseCase {

    private final MaintenanceRecordRepository repository;

    private final MaintenanceRecordAssembler assembler;

    private final AircraftRepository aircraftRepository;

    @Transactional(readOnly = true)
    public List<MaintenanceRecordOutputDto> execute(String aircraftRegistration) {

        if (aircraftRepository.findByRegistration_Registration(aircraftRegistration).isEmpty()) {
            throw new IllegalArgumentException("Aircraft not found: " + aircraftRegistration);
        }

        return repository.findAllByAircraftRegistration(aircraftRegistration)
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
    }
}