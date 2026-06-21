package isep.psoft.aisafe.maintenance.services.us221;

import isep.psoft.aisafe.maintenance.dto.TurnaroundItemDto;
import isep.psoft.aisafe.maintenance.dto.TurnaroundReportDto;
import isep.psoft.aisafe.maintenance.repositories.MaintenanceRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ViewAvgTurnaroundTimeUseCaseImpl implements ViewAvgTurnaroundTimeUseCase {

    private final MaintenanceRecordRepository repository;

    @Override
    public TurnaroundReportDto execute() {

        List<TurnaroundItemDto> items = repository.findAverageTurnaroundTimePerModel();

        return new TurnaroundReportDto(items);
    }
}