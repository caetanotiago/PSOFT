package isep.psoft.aisafe.airports.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record OperatingHoursRequest(
        @NotNull(message = "operates24Hours is required") Boolean operates24Hours,
        LocalTime opens,
        LocalTime closes
) {}
