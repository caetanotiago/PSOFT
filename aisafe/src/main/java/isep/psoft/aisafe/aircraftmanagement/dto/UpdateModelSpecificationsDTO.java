package isep.psoft.aisafe.aircraftmanagement.dto;

public class UpdateModelSpecificationsDTO {

    private Integer standardCapacity;
    private Double fuelCapacity;
    private Double maximumRange;
    private Double cruisingSpeed;

    public UpdateModelSpecificationsDTO() {}

    public Integer getStandardCapacity() { return standardCapacity; }
    public void setStandardCapacity(Integer standardCapacity) {
        this.standardCapacity = standardCapacity;
    }

    public Double getFuelCapacity() { return fuelCapacity; }
    public void setFuelCapacity(Double fuelCapacity) {
        this.fuelCapacity = fuelCapacity;
    }

    public Double getMaximumRange() { return maximumRange; }
    public void setMaximumRange(Double maximumRange) {
        this.maximumRange = maximumRange;
    }

    public Double getCruisingSpeed() { return cruisingSpeed; }
    public void setCruisingSpeed(Double cruisingSpeed) {
        this.cruisingSpeed = cruisingSpeed;
    }
}