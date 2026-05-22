package isep.psoft.aisafe.maintenance.services;

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
class MaintenanceRecordServiceTests {

    @Mock
    private MaintenanceRecordRepository recordRepository;

    @Mock
    private MaintenanceTemplateRepository templateRepository;

    @InjectMocks
    private MaintenanceRecordService service;

    @Test
    void whenTemplateExists_shouldCreateRecord() {
        // Arrange
        CreateRecordDTO dto = mock(CreateRecordDTO.class);
        when(dto.getAircraftRegistration()).thenReturn("CS-TWB");
        when(dto.getTemplateId()).thenReturn(1L);
        when(dto.getDescription()).thenReturn("Annual inspection");
        when(dto.getStartDate()).thenReturn(LocalDate.now());
        when(dto.getExpectedDurationMinutes()).thenReturn(480);
        when(dto.getComponentCategory()).thenReturn("ENGINE");

        MaintenanceRecord savedRecord = new MaintenanceRecord("CS-TWB", 1L, null, null);

        when(templateRepository.existsById(dto.getTemplateId())).thenReturn(true);
        when(recordRepository.save(any(MaintenanceRecord.class))).thenReturn(savedRecord);

        // Act
        MaintenanceRecord result = service.createRecord(dto);

        // Assert
        assertThat(result).isEqualTo(savedRecord);
        verify(recordRepository).save(any(MaintenanceRecord.class));
    }

    @Test
    void whenTemplateDoesNotExist_shouldThrowException() {
        // Arrange
        CreateRecordDTO dto = mock(CreateRecordDTO.class);
        when(dto.getTemplateId()).thenReturn(99L);

        when(templateRepository.existsById(dto.getTemplateId())).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> service.createRecord(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Maintenance Template does not exist.");

        verify(recordRepository, never()).save(any());
    }
}
