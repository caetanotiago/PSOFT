package isep.psoft.aisafe.maintenance.services.common;

import isep.psoft.aisafe.maintenance.assemblers.MaintenanceRecordAssembler;
import isep.psoft.aisafe.maintenance.domain.MaintenanceRecord;
import isep.psoft.aisafe.maintenance.dto.MaintenanceRecordOutputDto;
import isep.psoft.aisafe.maintenance.repositories.MaintenanceRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service("ViewMaintenanceRecordByIdUseCase")
public class ViewMaintenanceRecordByIdUseCase {

    @Autowired
    private MaintenanceRecordRepository repository;

    @Autowired
    private MaintenanceRecordAssembler assembler;

    @Transactional(readOnly = true)
    public Optional<MaintenanceRecordOutputDto> execute(Long recordId) {
        Optional<MaintenanceRecord> recordOpt = repository.findById(recordId);

        return recordOpt.map(assembler::toModel);
    }
}
