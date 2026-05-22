package isep.psoft.aisafe.maintenance.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import isep.psoft.aisafe.maintenance.domain.CompletionNotes;
import isep.psoft.aisafe.maintenance.domain.MaintenanceComponent;
import isep.psoft.aisafe.maintenance.domain.RecordDetails;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL) // Não mostra campos nulos (como completionNotes)
public class MaintenanceRecordOutputDto extends RepresentationModel<MaintenanceRecordOutputDto> {

    private final Long id;
    private final String aircraftRegistration;
    private final Long maintenanceTemplateId;
    private final RecordDetails recordDetails;
    private final MaintenanceComponent component;
    private final CompletionNotes completionNotes;

    public MaintenanceRecordOutputDto(Long id, String aircraftRegistration, Long maintenanceTemplateId,
                                      RecordDetails recordDetails, MaintenanceComponent component, CompletionNotes completionNotes) {
        this.id = id;
        this.aircraftRegistration = aircraftRegistration;
        this.maintenanceTemplateId = maintenanceTemplateId;
        this.recordDetails = recordDetails;
        this.component = component;
        this.completionNotes = completionNotes;
    }
}
