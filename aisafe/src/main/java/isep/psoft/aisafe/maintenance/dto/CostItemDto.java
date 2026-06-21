package isep.psoft.aisafe.maintenance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CostItemDto {

    private String identifier;

    private Double totalCost;
}