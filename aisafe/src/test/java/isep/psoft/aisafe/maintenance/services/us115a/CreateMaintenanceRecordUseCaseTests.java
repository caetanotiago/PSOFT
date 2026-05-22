package isep.psoft.aisafe.maintenance.services.us115a;

import isep.psoft.aisafe.maintenance.domain.MaintenanceRecord;
import isep.psoft.aisafe.maintenance.dto.CreateRecordDTO;
import isep.psoft.aisafe.maintenance.repositories.MaintenanceRecordRepository;
import isep.psoft.aisafe.maintenance.repositories.MaintenanceTemplateRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateMaintenanceRecordUseCaseTests {

    @Mock
    private MaintenanceRecordRepository recordRepository;

    @Mock
    private MaintenanceTemplateRepository templateRepository;

    @InjectMocks
    private CreateMaintenanceRecordUseCase useCase;

    @Test
    void whenTemplateExists_shouldCreateAndSaveRecord() {
        // Arrange
        CreateRecordDTO dto = new CreateRecordDTO("CS-TWB", 1L, "Annual inspection", LocalDate.now(), 480, "ENGINE");
        MaintenanceRecord savedRecord = mock(MaintenanceRecord.class);

        // Mocking the dependencies' behavior
        when(templateRepository.existsById(dto.getTemplateId())).thenReturn(true);
        when(recordRepository.save(any(MaintenanceRecord.class))).thenReturn(savedRecord);

        // Act
        MaintenanceRecord result = useCase.execute(dto);

        // Assert
        assertThat(result).isEqualTo(savedRecord);
        verify(templateRepository, times(1)).existsById(dto.getTemplateId());
        verify(recordRepository, times(1)).save(any(MaintenanceRecord.class));
    }

    @Test
    void whenTemplateDoesNotExist_shouldThrowException() {
        // Arrange
        CreateRecordDTO dto = new CreateRecordDTO("CS-TWB", 99L, "Non-existent template test", LocalDate.now(), 60, "AVIONICS");
        
        // Mocking the dependency to simulate the failure condition
        when(templateRepository.existsById(dto.getTemplateId())).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> useCase.execute(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Maintenance Template does not exist.");

        // Ensure we never tried to save if validation failed
        verify(recordRepository, never()).save(any());
    }
}
