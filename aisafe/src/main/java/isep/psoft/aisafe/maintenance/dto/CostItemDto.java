package isep.psoft.aisafe.maintenance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CostItemDto {

    // O identificador pode ser a matrícula (AIRCRAFT) ou o nome do modelo (MODEL)
    private String identifier;

    private Double totalCost;
}