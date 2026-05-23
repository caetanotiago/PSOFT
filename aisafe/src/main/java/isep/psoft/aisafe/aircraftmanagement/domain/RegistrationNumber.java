package isep.psoft.aisafe.aircraftmanagement.domain;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.regex.Pattern;

@Embeddable
public class RegistrationNumber implements Serializable {
    
    private String number;

    protected RegistrationNumber() {} // Obrigatório para o JPA

    public RegistrationNumber(String number) {
        // Valida formato aviation, ex: "CS-TKY" (1 a 3 letras, traço, 3 a 5 caracteres alfanuméricos)
        if (number == null || !Pattern.matches("^[A-Z]{1,3}-[A-Z0-9]{3,5}$", number)) {
            throw new IllegalArgumentException("Invalid registration number format. Expected format like 'CS-TKY'.");
        }
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    // Métodos equals() e hashCode() obrigatórios em classes usadas como @EmbeddedId
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegistrationNumber that = (RegistrationNumber) o;
        return Objects.equals(number, that.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }
}