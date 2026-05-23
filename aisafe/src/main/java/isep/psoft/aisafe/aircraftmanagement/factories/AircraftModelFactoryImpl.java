package isep.psoft.aisafe.aircraftmanagement.factories;

import isep.psoft.aisafe.aircraftmanagement.domain.AircraftModel;
import isep.psoft.aisafe.aircraftmanagement.domain.ModelDesignation;
import isep.psoft.aisafe.aircraftmanagement.domain.ModelSpecifications;

import org.springframework.stereotype.Component;

@Component
public class AircraftModelFactoryImpl implements AircraftModelFactory {

    @Override
    public AircraftModel createAircraftModel(String manufacturer, String modelName, 
                                             Integer standardSeatingCapacity, Double fuelCapacity, 
                                             Double maxRange, Double cruisingSpeed) {
        
        ModelDesignation designation = new ModelDesignation(manufacturer, modelName);
        ModelSpecifications specifications = new ModelSpecifications(standardSeatingCapacity, fuelCapacity, maxRange, cruisingSpeed);
        
        return new AircraftModel(designation, specifications);
    }
}
