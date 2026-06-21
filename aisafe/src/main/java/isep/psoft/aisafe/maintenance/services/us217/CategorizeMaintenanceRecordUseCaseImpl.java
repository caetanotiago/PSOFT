package isep.psoft.aisafe.maintenance.services.us217;

import isep.psoft.aisafe.maintenance.assemblers.MaintenanceRecordAssembler;
import isep.psoft.aisafe.maintenance.domain.MaintenanceComponent;
import isep.psoft.aisafe.maintenance.domain.MaintenanceRecord;
import isep.psoft.aisafe.maintenance.domain.MaintenanceRecordNotFoundException;
import isep.psoft.aisafe.maintenance.dto.MaintenanceRecordOutputDto;
import isep.psoft.aisafe.maintenance.repositories.MaintenanceRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategorizeMaintenanceRecordUseCaseImpl implements CategorizeMaintenanceRecordUseCase {

    private final MaintenanceRecordRepository maintenanceRecordRepository;
    private final MaintenanceRecordAssembler maintenanceRecordAssembler;

    @Override
    @Transactional
    public MaintenanceRecordOutputDto execute(Long recordId, String category, Long expectedVersion) {

        MaintenanceRecord record = maintenanceRecordRepository.findById(recordId)
                .orElseThrow(() -> new MaintenanceRecordNotFoundException(recordId));

        if (expectedVersion != null && !record.getVersion().equals(expectedVersion)) {
            throw new org.springframework.dao.OptimisticLockingFailureException("The record was updated by another user. Please refresh and try again.");
        }

        MaintenanceComponent newComponent = new MaintenanceComponent(category);

        record.updateComponent(newComponent);

        MaintenanceRecord savedRecord = maintenanceRecordRepository.save(record);
        return maintenanceRecordAssembler.toModel(savedRecord);
    }
}