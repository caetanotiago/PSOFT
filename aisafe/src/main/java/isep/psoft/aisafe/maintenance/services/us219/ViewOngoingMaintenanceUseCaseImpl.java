package isep.psoft.aisafe.maintenance.services.us219;

import isep.psoft.aisafe.maintenance.assemblers.MaintenanceRecordAssembler;
import isep.psoft.aisafe.maintenance.domain.MaintenanceRecord;
import isep.psoft.aisafe.maintenance.dto.MaintenanceRecordOutputDto;
import isep.psoft.aisafe.maintenance.repositories.MaintenanceRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ViewOngoingMaintenanceUseCaseImpl implements ViewOngoingMaintenanceUseCase {

    private final MaintenanceRecordRepository repository;
    private final MaintenanceRecordAssembler assembler;

    @Override
    public List<MaintenanceRecordOutputDto> execute() {

        List<MaintenanceRecord> ongoingRecords = repository.findByCompletionNotesIsNull();

        return ongoingRecords.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
    }
}