package isep.psoft.aisafe.maintenance.services.us115;

import isep.psoft.aisafe.aircraft.domain.AircraftModel;
import isep.psoft.aisafe.aircraft.repositories.AircraftModelRepository;
import isep.psoft.aisafe.maintenance.domain.MaintenanceTemplate;
import isep.psoft.aisafe.maintenance.dto.CreateTemplateDTO;
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

    @InjectMocks
    private CreateMaintenanceTemplateUseCase useCase;

    @Test
    void whenTemplateNameIsUniqueAndModelsExist_shouldCreateTemplate() {
        // Arrange
        CreateTemplateDTO dto = new CreateTemplateDTO("A-Check", "INSPECTION", 400, 90, List.of("Task 1"), List.of("A320"));
        AircraftModel model = mock(AircraftModel.class);
        MaintenanceTemplate savedTemplate = mock(MaintenanceTemplate.class);

        when(templateRepository.findByTemplateName(dto.getTemplateName())).thenReturn(Optional.empty());
        when(aircraftModelRepository.findByDesignationIn(dto.getApplicableModels())).thenReturn(List.of(model));
        when(templateRepository.save(any(MaintenanceTemplate.class))).thenReturn(savedTemplate);

        // Act
        MaintenanceTemplate result = useCase.execute(dto);

        // Assert
        assertThat(result).isEqualTo(savedTemplate);
        verify(templateRepository, times(1)).save(any(MaintenanceTemplate.class));
    }

    @Test
    void whenTemplateNameAlreadyExists_shouldThrowException() {
        // Arrange
        CreateTemplateDTO dto = new CreateTemplateDTO("A-Check", "INSPECTION", 400, 90, List.of(), List.of());
        when(templateRepository.findByTemplateName(dto.getTemplateName())).thenReturn(Optional.of(mock(MaintenanceTemplate.class)));

        // Act & Assert
        assertThatThrownBy(() -> useCase.execute(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("A template with this name already exists.");

        verify(templateRepository, never()).save(any());
    }

    @Test
    void whenNoAircraftModelsExist_shouldThrowException() {
        // Arrange
        CreateTemplateDTO dto = new CreateTemplateDTO("A-Check", "INSPECTION", 400, 90, List.of(), List.of("UnknownModel"));
        when(templateRepository.findByTemplateName(dto.getTemplateName())).thenReturn(Optional.empty());
        when(aircraftModelRepository.findByDesignationIn(dto.getApplicableModels())).thenReturn(List.of());

        // Act & Assert
        assertThatThrownBy(() -> useCase.execute(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("None of the provided Aircraft Models exist in the system.");

        verify(templateRepository, never()).save(any());
    }
}
