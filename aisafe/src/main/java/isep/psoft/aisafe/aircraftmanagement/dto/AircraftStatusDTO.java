package isep.psoft.aisafe.aircraftmanagement.dto;

public class AircraftStatusDTO {

    private String registrationNumber;
    private String status;

    public AircraftStatusDTO() {}

    public AircraftStatusDTO(String registrationNumber, String status) {
        this.registrationNumber = registrationNumber;
        this.status = status;
    }

    public String getRegistrationNumber() { return registrationNumber; }
    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}