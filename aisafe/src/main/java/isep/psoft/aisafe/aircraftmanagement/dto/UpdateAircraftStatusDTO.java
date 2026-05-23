package isep.psoft.aisafe.aircraftmanagement.dto;

public class UpdateAircraftStatusDTO {

    private String status;
    private Long version;

    public UpdateAircraftStatusDTO() {}

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}