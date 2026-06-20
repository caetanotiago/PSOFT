package isep.psoft.aisafe.maintenance.services.us220;

import isep.psoft.aisafe.maintenance.dto.MaintenanceCostReportDto;

public interface GenerateCostReportUseCase {

    /**
     * Gera um relatório de custos de manutenção agregados.
     *
     * @param reportType O tipo de agregação desejada ("AIRCRAFT" para matrícula, "MODEL" para modelo do avião).
     * @return DTO contendo o tipo de relatório e a lista de itens de custo associados.
     */
    MaintenanceCostReportDto execute(String reportType);
}