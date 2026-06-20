package isep.psoft.aisafe.maintenance.services.us222;

import isep.psoft.aisafe.aircraftmanagement.domain.Aircraft;
import isep.psoft.aisafe.aircraftmanagement.repositories.AircraftRepository;
import isep.psoft.aisafe.maintenance.domain.Alert;
import isep.psoft.aisafe.maintenance.domain.MaintenanceRecord;
import isep.psoft.aisafe.maintenance.domain.MaintenanceTemplate;
import isep.psoft.aisafe.maintenance.repositories.AlertRepository;
import isep.psoft.aisafe.maintenance.repositories.MaintenanceRecordRepository;
import isep.psoft.aisafe.maintenance.repositories.MaintenanceTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlertService {

    private final AircraftRepository aircraftRepository;
    private final MaintenanceTemplateRepository templateRepository;
    private final MaintenanceRecordRepository recordRepository;
    private final AlertRepository alertRepository;

    public void checkFleetMaintenance() {
        List<Aircraft> fleet = aircraftRepository.findAll();
        List<MaintenanceTemplate> templates = templateRepository.findAll();

        for (Aircraft aircraft : fleet) {
            for (MaintenanceTemplate template : templates) {
                String reg = aircraft.getRegistrationNumber().getNumber();

                MaintenanceRecord lastRecord = recordRepository
                        .findTopByAircraftRegistrationAndMaintenanceTemplateIdAndCompletionNotesIsNotNullOrderByCompletionNotes_CompletionDateDesc(
                                reg, template.getId()
                        ).orElse(null);

                if (isMaintenanceDue(template, lastRecord)) {
                    String msg = String.format("ALERTA: A aeronave %s precisa de manutenção agendada para o Template ID %d.", reg, template.getId());
                    alertRepository.save(new Alert(msg));
                }
            }
        }
    }

    // Regra matemática apenas para DIAS (para não interferir com o código do teu colega)
    public boolean isMaintenanceDue(MaintenanceTemplate template, MaintenanceRecord lastRecord) {
        LocalDate currentDate = LocalDate.now();
        LocalDate baseDate;

        if (lastRecord != null && lastRecord.getCompletionNotes() != null) {
            baseDate = lastRecord.getCompletionNotes().getCompletionDate();
        } else {
            // Se nunca teve manutenção, usamos uma data antiga para não dar erro
            baseDate = LocalDate.now().minusYears(1);
        }

        if (template.getInterval().getCalendarDays() > 0) {
            long daysPassed = ChronoUnit.DAYS.between(baseDate, currentDate);
            if (daysPassed >= template.getInterval().getCalendarDays()) {
                return true;
            }
        }

        return false; // As horas de voo virão para aqui no futuro!
    }
}