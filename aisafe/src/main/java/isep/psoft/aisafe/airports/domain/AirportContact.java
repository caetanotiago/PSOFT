package isep.psoft.aisafe.airports.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

// US208 / Conversation 014: an airport may have multiple contacts, each with a
// type (phone, email, fax, ...), a value, and an optional description/department.
@Embeddable
public class AirportContact {

    @Column(name = "contact_type")
    private String type;

    @Column(name = "contact_value")
    private String value;

    @Column(name = "contact_description")
    private String description;

    protected AirportContact() {}

    public AirportContact(String type, String value, String description) {
        if (type == null || type.isBlank())
            throw new IllegalArgumentException("Contact type is required");
        if (value == null || value.isBlank())
            throw new IllegalArgumentException("Contact value is required");
        this.type = type;
        this.value = value;
        this.description = description;
    }

    public String getType() { return type; }
    public String getValue() { return value; }
    public String getDescription() { return description; }
}