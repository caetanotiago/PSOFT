package isep.psoft.aisafe.maintenance.services.us218;

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
public class SearchMaintenanceRecordsUseCaseImpl implements SearchMaintenanceRecordsUseCase {

    private final MaintenanceRecordRepository repository;
    private final MaintenanceRecordAssembler assembler;

    @Override
    public List<MaintenanceRecordOutputDto> execute(String aircraftRegistration, String componentCategory, String status) {

        // 1. Normalizar e validar o filtro de Estado (Status)
        if (status != null && !status.trim().isEmpty()) {
            status = status.trim().toUpperCase();
            if (!status.equals("ONGOING") && !status.equals("COMPLETED")) {
                throw new IllegalArgumentException("O estado (status) tem de ser 'ONGOING' ou 'COMPLETED'.");
            }
        } else {
            status = null; // Garante que é null se enviarem apenas uma string vazia
        }

        // 2. Normalizar o filtro de Categoria de Componente
        if (componentCategory != null && !componentCategory.trim().isEmpty()) {
            componentCategory = componentCategory.trim().toUpperCase();
        } else {
            componentCategory = null;
        }

        // 3. Normalizar a Matrícula
        if (aircraftRegistration != null && aircraftRegistration.trim().isEmpty()) {
            aircraftRegistration = null;
        }

        // 4. Chamar a BD usando a Query Dinâmica
        List<MaintenanceRecord> records = repository.searchRecords(aircraftRegistration, componentCategory, status);

        // 5. Converter a lista de Entidades para DTOs e retornar
        return records.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
    }
}