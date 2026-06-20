package isep.psoft.aisafe.maintenance.services.us219;

import isep.psoft.aisafe.maintenance.dto.MaintenanceRecordOutputDto;

import java.util.List;

public interface ViewOngoingMaintenanceUseCase {

    /**
     * Retorna uma lista de todas as atividades de manutenção em curso na frota.
     * Uma atividade em curso é definida pela ausência de notas de conclusão (CompletionNotes == null).
     *
     * @return Lista de registos de manutenção em curso, convertidos em DTO.
     */
    List<MaintenanceRecordOutputDto> execute();
}