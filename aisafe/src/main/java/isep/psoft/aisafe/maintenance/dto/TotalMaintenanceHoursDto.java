package isep.psoft.aisafe.maintenance.dto;

import lombok.Getter;

@Getter
public class TotalMaintenanceHoursDto {

    private final double totalHours;

    public TotalMaintenanceHoursDto(double totalHours) {
        this.totalHours = totalHours;
    }
}
