package isep.psoft.aisafe.aircraftmanagement.domain;

public enum ManufacturerConfig {
    AIRBUS("Airbus"),
    BOEING("Boeing"),
    EMBRAER("Embraer");

    private final String manufacturerName;

    ManufacturerConfig(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public static boolean isValid(String name) {
        for (ManufacturerConfig config : values()) {
            if (config.getManufacturerName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }
}