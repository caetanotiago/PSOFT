package isep.psoft.aisafe.airports.dto;

import org.springframework.hateoas.EntityModel;

import java.util.List;

public record AirportGroupDTO(String groupKey, List<EntityModel<AirportSummaryResponseDTO>> airports) {}
