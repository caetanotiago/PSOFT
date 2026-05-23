package isep.psoft.aisafe.aircraftmanagement.domain;

import jakarta.persistence.Embeddable;

@Embeddable
public class ModelSpecifications {

    private Integer standardCapacity;
    private Double fuelCapacity;
    private Double maximumRange;
    private Double cruisingSpeed;

    protected ModelSpecifications() {} // Obrigatório para o JPA

    public ModelSpecifications(Integer standardCapacity, Double fuelCapacity, Double maximumRange, Double cruisingSpeed) {
        if (standardCapacity == null || standardCapacity <= 0) {
            throw new IllegalArgumentException("Standard capacity must be strictly positive.");
        }
        if (fuelCapacity == null || fuelCapacity <= 0) {
            throw new IllegalArgumentException("Fuel capacity must be strictly positive.");
        }
        if (maximumRange == null || maximumRange <= 0) {
            throw new IllegalArgumentException("Maximum range must be strictly positive.");
        }
        if (cruisingSpeed == null || cruisingSpeed <= 0) {
            throw new IllegalArgumentException("Cruising speed must be strictly positive.");
        }
        
        this.standardCapacity = standardCapacity;
        this.fuelCapacity = fuelCapacity;
        this.maximumRange = maximumRange;
        this.cruisingSpeed = cruisingSpeed;
    }

    public Integer getStandardCapacity() { return standardCapacity; }
    
    public Double getFuelCapacity() { return fuelCapacity; }
    
    public Double getMaximumRange() { return maximumRange; }
    
    public Double getCruisingSpeed() { return cruisingSpeed; }
}