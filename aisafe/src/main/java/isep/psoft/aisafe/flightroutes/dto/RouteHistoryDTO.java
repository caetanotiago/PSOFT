package isep.psoft.aisafe.flightroutes.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RouteHistoryDTO {
    private String changeDate;
    private String description;
    private Double previousDistance;
}