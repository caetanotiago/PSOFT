package isep.psoft.aisafe.maintenance.services.us116;

import isep.psoft.aisafe.maintenance.assemblers.MaintenanceRecordAssembler;
import isep.psoft.aisafe.maintenance.domain.MaintenanceRecord;
import isep.psoft.aisafe.maintenance.dto.MaintenanceRecordOutputDto;
import isep.psoft.aisafe.maintenance.repositories.MaintenanceRecordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Modern approach for Mockito with JUnit 5
class ViewAircraftMaintenanceRecordsUseCaseTests {

    @Mock
    private MaintenanceRecordRepository repository;

    @Mock
    private MaintenanceRecordAssembler assembler;

    @InjectMocks
    private ViewAircraftMaintenanceRecordsUseCase useCase;

    @Test
    void whenAircraftHasRecords_shouldReturnDtoList() {
        // Arrange
        String registration = "CS-TVA";
        MaintenanceRecord record1 = mock(MaintenanceRecord.class);
        MaintenanceRecordOutputDto dto1 = mock(MaintenanceRecordOutputDto.class);

        when(repository.findAllByAircraftRegistration(registration)).thenReturn(List.of(record1));
        when(assembler.toModel(record1)).thenReturn(dto1);

        // Act
        List<MaintenanceRecordOutputDto> result = useCase.execute(registration);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(dto1);
        verify(repository, times(1)).findAllByAircraftRegistration(registration);
        verify(assembler, times(1)).toModel(record1);
    }

    @Test
    void whenAircraftHasNoRecords_shouldReturnEmptyList() {
        // Arrange
        String registration = "CS-TVB";
        when(repository.findAllByAircraftRegistration(registration)).thenReturn(Collections.emptyList());

        // Act
        List<MaintenanceRecordOutputDto> result = useCase.execute(registration);

        // Assert
        assertThat(result).isEmpty();
        verify(repository, times(1)).findAllByAircraftRegistration(registration);
        verify(assembler, never()).toModel(any());
    }
}
