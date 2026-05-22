package isep.psoft.aisafe.maintenance.services.us117;

import isep.psoft.aisafe.maintenance.dto.TotalMaintenanceHoursDto;
import isep.psoft.aisafe.maintenance.repositories.MaintenanceRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("ViewTotalMaintenanceHoursUseCase")
public class ViewTotalMaintenanceHoursUseCase {

    @Autowired
    private MaintenanceRecordRepository repository;

    private static final int MINUTES_PER_HOUR = 60;

    @Transactional(readOnly = true)
    public TotalMaintenanceHoursDto execute() {
        Long totalMinutes = repository.sumTotalExpectedDurationMinutes();
        double totalHours = (double) totalMinutes / MINUTES_PER_HOUR;
        return new TotalMaintenanceHoursDto(totalHours);
    }
}
