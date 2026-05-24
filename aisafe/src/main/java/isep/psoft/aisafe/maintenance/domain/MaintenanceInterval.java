package isep.psoft.aisafe.maintenance.domain;

import jakarta.persistence.Embeddable;

@Embeddable
public class MaintenanceInterval {

    private Integer flightHours;
    private Integer calendarDays;

    protected MaintenanceInterval() {
        // JPA requirement
    }

    public MaintenanceInterval(Integer flightHours, Integer calendarDays) {
        if (flightHours != null && flightHours < 0) {
            throw new IllegalArgumentException("Flight hours cannot be negative");
        }
        if (calendarDays != null && calendarDays < 0) {
            throw new IllegalArgumentException("Calendar days cannot be negative");
        }
        if (flightHours == null && calendarDays == null) {
            throw new IllegalArgumentException("At least one interval (hours or days) must be provided");
        }
        this.flightHours = flightHours;
        this.calendarDays = calendarDays;
    }

    public Integer getFlightHours() { return flightHours; }
    public Integer getCalendarDays() { return calendarDays; }
}