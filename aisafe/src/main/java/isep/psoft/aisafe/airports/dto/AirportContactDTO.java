package isep.psoft.aisafe.airports.dto;

import isep.psoft.aisafe.airports.domain.AirportContact;

public record AirportContactDTO(String type, String value, String description) {

    public static AirportContactDTO from(AirportContact contact) {
        return new AirportContactDTO(contact.getType(), contact.getValue(), contact.getDescription());
    }
}
