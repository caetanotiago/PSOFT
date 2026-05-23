package isep.psoft.aisafe.aircraftmanagement.factories;

import isep.psoft.aisafe.aircraftmanagement.domain.AircraftModel;

public interface AircraftModelFactory {
    
    AircraftModel createAircraftModel(String manufacturer, String modelName, 
                                      Integer standardSeatingCapacity, Double fuelCapacity, 
                                      Double maxRange, Double cruisingSpeed);

}