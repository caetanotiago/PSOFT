package isep.psoft.aisafe.maintenance.services.us218;

import isep.psoft.aisafe.maintenance.dto.MaintenanceRecordOutputDto;

import java.util.List;

public interface SearchMaintenanceRecordsUseCase {

    /**
     * Pesquisa registos de manutenção com base em filtros opcionais.
     *
     * @param aircraftRegistration Matrícula do avião (opcional)
     * @param componentCategory    Categoria do componente (opcional)
     * @param status               Estado da manutenção ('ONGOING' ou 'COMPLETED') (opcional)
     * @return Lista de registos de manutenção que cumprem os critérios, convertidos em DTO
     */
    List<MaintenanceRecordOutputDto> execute(String aircraftRegistration, String componentCategory, String status);
}