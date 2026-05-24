package isep.psoft.aisafe.airports.dto;

import jakarta.validation.constraints.NotBlank;

public record AddCertificationRequest(
        @NotBlank(message = "Manufacturer is required") String manufacturer,
        @NotBlank(message = "Model name is required") String modelName
) {}
