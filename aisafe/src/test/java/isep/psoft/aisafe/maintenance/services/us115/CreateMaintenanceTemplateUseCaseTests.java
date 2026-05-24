package isep.psoft.aisafe.maintenance.services.us115;

import isep.psoft.aisafe.aircraftmanagement.domain.AircraftModel;
import isep.psoft.aisafe.aircraftmanagement.repositories.AircraftModelRepository;
import isep.psoft.aisafe.maintenance.assemblers.TemplateAssembler;
import isep.psoft.aisafe.maintenance.domain.MaintenanceTemplate;
import isep.psoft.aisafe.maintenance.dto.CreateTemplateDTO;
import isep.psoft.aisafe.maintenance.dto.TemplateDTO;
import isep.psoft.aisafe.maintenance.repositories.MaintenanceTemplateRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateMaintenanceTemplateUseCaseTests {

    @Mock
    private MaintenanceTemplateRepository templateRepository;

    @Mock
    private AircraftModelRepository aircraftModelRepository;

    @Mock
    private TemplateAssembler assembler;

    @InjectMocks
    private CreateMaintenanceTemplateUseCase useCase;

    @Test
    void whenAllDataIsValid_shouldCreateTemplate() {

        String modelName = "A320";
        CreateTemplateDTO dto = new CreateTemplateDTO("A-Check", "INSPECTION", 400, 90, List.of("Task 1"), List.of(modelName));
        AircraftModel model = mock(AircraftModel.class);
        MaintenanceTemplate savedTemplate = mock(MaintenanceTemplate.class);
        TemplateDTO expectedDto = mock(TemplateDTO.class);

        when(templateRepository.findByTemplateName(dto.getTemplateName())).thenReturn(Optional.empty());
        when(aircraftModelRepository.findByDesignationModelName(modelName)).thenReturn(Optional.of(model));
        when(templateRepository.save(any(MaintenanceTemplate.class))).thenReturn(savedTemplate);

        when(assembler.toDTO(savedTemplate)).thenReturn(expectedDto);

        TemplateDTO result = useCase.execute(dto);

        assertThat(result).isEqualTo(expectedDto);
        verify(templateRepository, times(1)).save(any(MaintenanceTemplate.class));
        verify(assembler, times(1)).toDTO(savedTemplate);
    }

    @Test
    void whenAnAircraftModelIsNotFound_shouldThrowException() {

        String validModelName = "A320";
        String invalidModelName = "B747-NonExistent";
        CreateTemplateDTO dto = new CreateTemplateDTO("A-Check", "INSPECTION", 400, 90, List.of(), List.of(validModelName, invalidModelName));
        
        when(aircraftModelRepository.findByDesignationModelName(validModelName)).thenReturn(Optional.of(mock(AircraftModel.class)));
        when(aircraftModelRepository.findByDesignationModelName(invalidModelName)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Aircraft Model '" + invalidModelName + "' not found.");

        verify(templateRepository, never()).save(any());
    }

    @Test
    void whenTemplateNameAlreadyExists_shouldThrowException() {

        CreateTemplateDTO dto = new CreateTemplateDTO("A-Check", "INSPECTION", 400, 90, List.of(), List.of("A320"));
        when(templateRepository.findByTemplateName(dto.getTemplateName())).thenReturn(Optional.of(mock(MaintenanceTemplate.class)));

        assertThatThrownBy(() -> useCase.execute(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("A template with this name already exists.");

        verify(aircraftModelRepository, never()).findByDesignationModelName(anyString());
        verify(templateRepository, never()).save(any());
    }
}
