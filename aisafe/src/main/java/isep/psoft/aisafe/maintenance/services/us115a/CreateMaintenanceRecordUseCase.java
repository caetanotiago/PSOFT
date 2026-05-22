package isep.psoft.aisafe.maintenance.services.us115a;

import isep.psoft.aisafe.maintenance.domain.MaintenanceComponent;
import isep.psoft.aisafe.maintenance.domain.MaintenanceRecord;
import isep.psoft.aisafe.maintenance.domain.RecordDetails;
import isep.psoft.aisafe.maintenance.dto.CreateRecordDTO;
import isep.psoft.aisafe.maintenance.repositories.MaintenanceRecordRepository;
import isep.psoft.aisafe.maintenance.repositories.MaintenanceTemplateRepository;
// import isep.psoft.aisafe.aircraft.repositories.AircraftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("CreateMaintenanceRecordUseCase")
public class CreateMaintenanceRecordUseCase {

    @Autowired
    private MaintenanceRecordRepository recordRepository;

    @Autowired
    private MaintenanceTemplateRepository templateRepository;
    
    // @Autowired
    // private AircraftRepository aircraftRepository;

    @Transactional
    public MaintenanceRecord execute(CreateRecordDTO dto) {

        // 1. Verificar se a Aeronave existe pela matrícula (atualmente comentado)
        /* 
        boolean aircraftExists = aircraftRepository.existsByRegistrationNumber(dto.getAircraftRegistration());
        if (!aircraftExists) {
            throw new IllegalArgumentException("Aircraft with the provided registration does not exist.");
        }
        */

        // 2. Verificar se o Template existe pelo ID
        boolean templateExists = templateRepository.existsById(dto.getTemplateId());
        if (!templateExists) {
            throw new IllegalArgumentException("Maintenance Template does not exist.");
        }

        // 3. Criar os Value Objects
        RecordDetails details = new RecordDetails(dto.getDescription(), dto.getStartDate(), dto.getExpectedDurationMinutes());
        MaintenanceComponent component = new MaintenanceComponent(dto.getComponentCategory());

        // 4. Criar a Entidade e Gravar
        MaintenanceRecord newRecord = new MaintenanceRecord(
                dto.getAircraftRegistration(),
                dto.getTemplateId(),
                details,
                component
        );

        return recordRepository.save(newRecord);
    }
}
