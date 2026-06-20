package isep.psoft.aisafe.airports.dto;

import isep.psoft.aisafe.airports.domain.Facility;

public record FacilityDTO(String type, String identifier, String description) {

    public static FacilityDTO from(Facility facility) {
        return new FacilityDTO(facility.getType(), facility.getIdentifier(), facility.getDescription());
    }
}
