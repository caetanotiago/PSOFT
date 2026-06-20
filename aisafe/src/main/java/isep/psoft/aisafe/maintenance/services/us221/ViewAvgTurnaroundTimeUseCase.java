package isep.psoft.aisafe.maintenance.services.us221;

import isep.psoft.aisafe.maintenance.dto.TurnaroundReportDto;

public interface ViewAvgTurnaroundTimeUseCase {

    /**
     * Gera o relatório do Tempo Médio de Resolução (Turnaround Time)
     * agrupado por modelo de avião.
     *
     * @return DTO contendo a lista com as médias de tempo.
     */
    TurnaroundReportDto execute();
}