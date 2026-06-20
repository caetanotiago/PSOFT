package isep.psoft.aisafe.maintenance.dto;

import jakarta.validation.constraints.NotBlank;

public class CategorizeRecordInputDto {

    @NotBlank(message = "The component category cannot be blank.")
    private String category;

    protected CategorizeRecordInputDto() {
        // Construtor vazio necessário para o Spring/Jackson conseguir converter o JSON
    }

    public CategorizeRecordInputDto(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}