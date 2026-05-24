package isep.psoft.aisafe.maintenance.services.us116;

import isep.psoft.aisafe.aircraftmanagement.domain.RegistrationNumber;
import isep.psoft.aisafe.aircraftmanagement.repositories.AircraftRepository;
import isep.psoft.aisafe.maintenance.assemblers.MaintenanceRecordAssembler;
import isep.psoft.aisafe.maintenance.dto.MaintenanceRecordOutputDto;
import isep.psoft.aisafe.maintenance.repositories.MaintenanceRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service("ViewAircraftMaintenanceRecordsUseCase")
public class ViewAircraftMaintenanceRecordsUseCase {

    @Autowired
    private MaintenanceRecordRepository repository;

    @Autowired
    private MaintenanceRecordAssembler assembler;

    @Autowired
    private AircraftRepository aircraftRepository;

    @Transactional(readOnly = true)
    public List<MaintenanceRecordOutputDto> execute(String aircraftRegistration) {

        if (!aircraftRepository.existsById(new RegistrationNumber(aircraftRegistration))) {
            throw new IllegalArgumentException("Aircraft not found: " + aircraftRegistration);
        }

        return repository.findAllByAircraftRegistration(aircraftRegistration)
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
    }
}