package isep.psoft.aisafe.airports.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;

// US207: structured facility data (terminals, gates, services) — Conversation 008
// confirmed the customer prefers structured data over a free-text field.
@Embeddable
public class Facility {

    @Column(name = "facility_type")
    private String type;

    @Column(name = "facility_identifier")
    private String identifier;

    @Column(name = "facility_description")
    private String description;

    protected Facility() {}

    public Facility(String type, String identifier, String description) {
        if (type == null || type.isBlank())
            throw new IllegalArgumentException("Facility type is required");
        if (identifier == null || identifier.isBlank())
            throw new IllegalArgumentException("Facility identifier is required");
        this.type = type;
        this.identifier = identifier;
        this.description = description;
    }

    public String getType() { return type; }
    public String getIdentifier() { return identifier; }
    public String getDescription() { return description; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Facility that)) return false;
        return Objects.equals(type, that.type) && Objects.equals(identifier, that.identifier);
    }

    @Override
    public int hashCode() { return Objects.hash(type, identifier); }
}