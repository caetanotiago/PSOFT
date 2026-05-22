package isep.psoft.aisafe.maintenance.dto;

import lombok.Getter;

@Getter
public class CompleteRecordInputDto {
    private String notes;

    // Jackson precisa de um construtor default para deserialização
    public CompleteRecordInputDto() {}

    public CompleteRecordInputDto(String notes) {
        this.notes = notes;
    }
}
