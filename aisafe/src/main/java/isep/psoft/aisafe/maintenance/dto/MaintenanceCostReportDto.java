package isep.psoft.aisafe.maintenance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceCostReportDto {

    private String reportType; // Guardará "AIRCRAFT" ou "MODEL"
    private List<CostItemDto> items;

}