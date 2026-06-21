package isep.psoft.aisafe.maintenance.services.us221;

import isep.psoft.aisafe.maintenance.dto.TurnaroundItemDto;
import isep.psoft.aisafe.maintenance.dto.TurnaroundReportDto;
import isep.psoft.aisafe.maintenance.repositories.MaintenanceRecordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ViewAvgTurnaroundTimeUseCaseImplTest {

    @Mock
    private MaintenanceRecordRepository repository;

    @InjectMocks
    private ViewAvgTurnaroundTimeUseCaseImpl useCase;

    @Test
    void execute_shouldReturnTurnaroundReport_whenRecordsExist() {
        // Arrange
        TurnaroundItemDto mockItem = mock(TurnaroundItemDto.class);
        List<TurnaroundItemDto> expectedItems = List.of(mockItem);

        when(repository.findAverageTurnaroundTimePerModel()).thenReturn(expectedItems);

        // Act
        TurnaroundReportDto result = useCase.execute();

        // Assert
        assertThat(result).isNotNull();


        verify(repository, times(1)).findAverageTurnaroundTimePerModel();
    }

    @Test
    void execute_shouldReturnEmptyReport_whenNoRecordsExist() {
        // Arrange
        when(repository.findAverageTurnaroundTimePerModel()).thenReturn(Collections.emptyList());

        // Act
        TurnaroundReportDto result = useCase.execute();

        // Assert
        assertThat(result).isNotNull();
        verify(repository, times(1)).findAverageTurnaroundTimePerModel();
    }
}