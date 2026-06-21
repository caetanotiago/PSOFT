package isep.psoft.aisafe.maintenance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TurnaroundReportDto {

    private List<TurnaroundItemDto> items;

}