package isep.psoft.aisafe.maintenance.services;

import isep.psoft.aisafe.maintenance.domain.MaintenanceComponent;
import isep.psoft.aisafe.maintenance.domain.MaintenanceRecord;
import isep.psoft.aisafe.maintenance.domain.RecordDetails;
import isep.psoft.aisafe.maintenance.dto.CreateRecordDTO;
import isep.psoft.aisafe.maintenance.repositories.MaintenanceRecordRepository;
import isep.psoft.aisafe.maintenance.repositories.MaintenanceTemplateRepository;
// import isep.psoft.aisafe.aircraft.repositories.AircraftRepository; // Descomenta quando tiveres o repositório do teu colega
import org.springframework.stereotype.Service;

@Service
public class MaintenanceRecordService {

    private final MaintenanceRecordRepository recordRepository;
    private final MaintenanceTemplateRepository templateRepository;
    // private final AircraftRepository aircraftRepository;

    public MaintenanceRecordService(MaintenanceRecordRepository recordRepository,
                                    MaintenanceTemplateRepository templateRepository) {
        // , AircraftRepository aircraftRepository) {
        this.recordRepository = recordRepository;
        this.templateRepository = templateRepository;
        // this.aircraftRepository = aircraftRepository;
    }

    public MaintenanceRecord createRecord(CreateRecordDTO dto) {

        // 1. Verificar se a Aeronave existe pela matrícula
        /* boolean aircraftExists = aircraftRepository.existsByRegistrationNumber(dto.getAircraftRegistration());
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