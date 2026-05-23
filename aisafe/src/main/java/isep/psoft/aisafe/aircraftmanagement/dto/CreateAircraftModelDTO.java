package isep.psoft.aisafe.aircraftmanagement.dto;

public class CreateAircraftModelDTO {

    private String manufacturer;
    private String modelName;
    private Integer standardCapacity;
    private Double fuelCapacity;
    private Double maximumRange;
    private Double cruisingSpeed;

    public CreateAircraftModelDTO() {}

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public Integer getStandardCapacity() {
        return standardCapacity;
    }

    public void setStandardCapacity(Integer standardCapacity) {
        this.standardCapacity = standardCapacity;
    }

    public Double getFuelCapacity() {
        return fuelCapacity;
    }

    public void setFuelCapacity(Double fuelCapacity) {
        this.fuelCapacity = fuelCapacity;
    }

    public Double getMaximumRange() {
        return maximumRange;
    }

    public void setMaximumRange(Double maximumRange) {
        this.maximumRange = maximumRange;
    }

    public Double getCruisingSpeed() {
        return cruisingSpeed;
    }

    public void setCruisingSpeed(Double cruisingSpeed) {
        this.cruisingSpeed = cruisingSpeed;
    }
}