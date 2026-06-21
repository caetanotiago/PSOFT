package isep.psoft.aisafe.maintenance.dto;

import jakarta.validation.constraints.NotBlank;

public class CategorizeRecordInputDto {

    @NotBlank(message = "The component category cannot be blank.")
    private String category;

    protected CategorizeRecordInputDto() {

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