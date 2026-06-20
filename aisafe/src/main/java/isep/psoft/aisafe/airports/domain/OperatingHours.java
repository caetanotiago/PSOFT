package isep.psoft.aisafe.airports.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.time.LocalTime;

// US208: either the airport operates 24 hours, or it has a fixed opens/closes window.
@Embeddable
public class OperatingHours {

    @Column(name = "operates_24_hours")
    private boolean operates24Hours;

    @Column(name = "opens")
    private LocalTime opens;

    @Column(name = "closes")
    private LocalTime closes;

    protected OperatingHours() {}

    public OperatingHours(boolean operates24Hours, LocalTime opens, LocalTime closes) {
        if (!operates24Hours) {
            if (opens == null || closes == null)
                throw new IllegalArgumentException("Opens and closes times are required when not operating 24 hours");
            if (!opens.isBefore(closes))
                throw new IllegalArgumentException("Opens time must be before closes time");
        }
        this.operates24Hours = operates24Hours;
        this.opens = operates24Hours ? null : opens;
        this.closes = operates24Hours ? null : closes;
    }

    public boolean isOperates24Hours() { return operates24Hours; }
    public LocalTime getOpens() { return opens; }
    public LocalTime getCloses() { return closes; }
}