package isep.psoft.aisafe.maintenance.services.us115a;

import isep.psoft.aisafe.aircraftmanagement.domain.RegistrationNumber;
import isep.psoft.aisafe.aircraftmanagement.repositories.AircraftRepository;
import isep.psoft.aisafe.maintenance.assemblers.MaintenanceRecordAssembler;
import isep.psoft.aisafe.maintenance.domain.MaintenanceComponent;
import isep.psoft.aisafe.maintenance.domain.MaintenanceRecord;
import isep.psoft.aisafe.maintenance.domain.RecordDetails;
import isep.psoft.aisafe.maintenance.dto.CreateRecordDTO;
import isep.psoft.aisafe.maintenance.dto.MaintenanceRecordOutputDto;
import isep.psoft.aisafe.maintenance.repositories.MaintenanceRecordRepository;
import isep.psoft.aisafe.maintenance.repositories.MaintenanceTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("CreateMaintenanceRecordUseCase")
public class CreateMaintenanceRecordUseCase {

    @Autowired
    private MaintenanceRecordRepository recordRepository;

    @Autowired
    private MaintenanceTemplateRepository templateRepository;

    @Autowired
    private AircraftRepository aircraftRepository;

    @Autowired
    private MaintenanceRecordAssembler assembler;

    @Transactional
    public MaintenanceRecordOutputDto execute(CreateRecordDTO dto) {

        RegistrationNumber regNum = new RegistrationNumber(dto.getAircraftRegistration());

        if (!aircraftRepository.existsByRegistrationNumber(regNum)) {
            throw new IllegalArgumentException("Aircraft with registration " + dto.getAircraftRegistration() + " does not exist.");
        }

        if (!templateRepository.existsById(dto.getTemplateId())) {
            throw new IllegalArgumentException("Maintenance Template with ID " + dto.getTemplateId() + " does not exist.");
        }

        RecordDetails details = new RecordDetails(dto.getDescription(), dto.getStartDate(), dto.getExpectedDurationMinutes());
        MaintenanceComponent component = new MaintenanceComponent(dto.getComponentCategory());

        MaintenanceRecord newRecord = new MaintenanceRecord(
                dto.getAircraftRegistration(),
                dto.getTemplateId(),
                details,
                component
        );

        MaintenanceRecord savedRecord = recordRepository.save(newRecord);

        return assembler.toModel(savedRecord);
    }
}
