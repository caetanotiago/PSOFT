package isep.psoft.aisafe.airports.dto;

import isep.psoft.aisafe.domain.shared.ModelDesignation;

public record ModelDesignationDTO(String manufacturer, String modelName) {

    public static ModelDesignationDTO from(ModelDesignation md) {
        return new ModelDesignationDTO(md.getManufacturer(), md.getModelName());
    }
}
