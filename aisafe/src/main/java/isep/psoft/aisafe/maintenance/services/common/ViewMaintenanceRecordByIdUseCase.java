package isep.psoft.aisafe.maintenance.services.common;

import isep.psoft.aisafe.maintenance.assemblers.MaintenanceRecordAssembler;
import isep.psoft.aisafe.maintenance.domain.MaintenanceRecord;
import isep.psoft.aisafe.maintenance.dto.MaintenanceRecordOutputDto;
import isep.psoft.aisafe.maintenance.repositories.MaintenanceRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service("ViewMaintenanceRecordByIdUseCase")
@RequiredArgsConstructor
public class ViewMaintenanceRecordByIdUseCase {

    private final MaintenanceRecordRepository repository;

    private final MaintenanceRecordAssembler assembler;

    @Transactional(readOnly = true)
    public Optional<MaintenanceRecordOutputDto> execute(Long recordId) {
        Optional<MaintenanceRecord> recordOpt = repository.findById(recordId);

        return recordOpt.map(assembler::toModel);
    }
}
