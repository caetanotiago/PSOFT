package isep.psoft.aisafe.airports.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

// Slide "Value Objects in the Database" (7_REST_Web_API_DDD_Library):
// VOs devem ser persistidos via @Embedded/@EmbeddedId, não descartados após validação.
@Embeddable
public class IATACode implements Serializable {

    @Column(name = "iata_code", length = 3)
    private String code;

    protected IATACode() {}

    public IATACode(String code) {
        if (code == null || !code.matches("[A-Z]{3}"))
            throw new IllegalArgumentException("IATA code must be exactly 3 uppercase letters, got: " + code);
        this.code = code;
    }

    public String getCode() { return code; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IATACode that)) return false;
        return Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() { return Objects.hash(code); }

    @Override
    public String toString() { return code; }
}
