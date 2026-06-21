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

        // 1. Chamar o repositório para executar a query com a função matemática AVG()
        List<TurnaroundItemDto> items = repository.findAverageTurnaroundTimePerModel();

        // 2. Embrulhar a lista de resultados no DTO final do relatório e devolver
        return new TurnaroundReportDto(items);
    }
}