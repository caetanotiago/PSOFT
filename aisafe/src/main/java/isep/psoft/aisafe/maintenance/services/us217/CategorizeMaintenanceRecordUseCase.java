package isep.psoft.aisafe.maintenance.services.us217;

import isep.psoft.aisafe.maintenance.dto.MaintenanceRecordOutputDto;

public interface CategorizeMaintenanceRecordUseCase {

    /**
     * Atualiza a categoria de um registo de manutenção.
     *
     * @param recordId O ID do registo a atualizar.
     * @param category A nova categoria do componente (e.g., "ENGINE").
     * @param expectedVersion A versão atual do registo para controlo de concorrência (optimistic locking).
     * @return O DTO do registo atualizado.
     */
    MaintenanceRecordOutputDto execute(Long recordId, String category, Long expectedVersion);
}