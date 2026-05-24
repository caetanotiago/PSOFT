package isep.psoft.aisafe.maintenance.dto;

import jakarta.validation.constraints.NotBlank;

public class CompleteRecordInputDto {

    @NotBlank(message = "Completion notes cannot be empty")
    private String notes;

    public CompleteRecordInputDto() {}

    public CompleteRecordInputDto(String notes) {
        this.notes = notes;
    }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}