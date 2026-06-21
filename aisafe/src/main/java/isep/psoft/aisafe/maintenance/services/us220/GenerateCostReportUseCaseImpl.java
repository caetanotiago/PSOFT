package isep.psoft.aisafe.maintenance.services.us220;

import isep.psoft.aisafe.maintenance.dto.CostItemDto;
import isep.psoft.aisafe.maintenance.dto.MaintenanceCostReportDto;
import isep.psoft.aisafe.maintenance.repositories.MaintenanceRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenerateCostReportUseCaseImpl implements GenerateCostReportUseCase {

    private final MaintenanceRecordRepository repository;

    @Override
    public MaintenanceCostReportDto execute(String reportType) {

        if (reportType == null || reportType.trim().isEmpty()) {
            throw new IllegalArgumentException("O tipo de relatório (reportType) é obrigatório ('AIRCRAFT' ou 'MODEL').");
        }

        String normalizedType = reportType.trim().toUpperCase();
        List<CostItemDto> items;

        switch (normalizedType) {
            case "AIRCRAFT":
                items = repository.calculateCostsPerAircraft();
                break;
            case "MODEL":
                items = repository.calculateCostsPerModel();
                break;
            default:
                throw new IllegalArgumentException("Tipo de relatório inválido. Deve ser 'AIRCRAFT' ou 'MODEL'.");
        }

        return new MaintenanceCostReportDto(normalizedType, items);
    }
}