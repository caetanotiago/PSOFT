package isep.psoft.aisafe.aircraftmanagement.dto;

public class AircraftModelUtilizationDTO {

    private String modelName;
    private Long value; // nº de voos OU minutos de voo, consoante a métrica

    public AircraftModelUtilizationDTO() {}

    // Construtor usado diretamente pelo JPQL
    public AircraftModelUtilizationDTO(String modelName, Long value) {
        this.modelName = modelName;
        this.value = value;
    }

    public String getModelName() { return modelName; }
    public void setModelName(String modelName) { this.modelName = modelName; }

    public Long getValue() { return value; }
    public void setValue(Long value) { this.value = value; }
}