package isep.psoft.aisafe.airports.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Runway {

    @Column(name = "runway_name")
    private String name;

    @Column(name = "runway_length")
    private Double length;

    @Column(name = "runway_orientation")
    private String orientation;

    protected Runway() {}

    public Runway(String name, Double length, String orientation) {
        if (length == null || length <= 0)
            throw new IllegalArgumentException("Runway length must be a positive value");
        this.name = name;
        this.length = length;
        this.orientation = orientation;
    }

    public String getName() { return name; }
    public Double getLength() { return length; }
    public String getOrientation() { return orientation; }
}
