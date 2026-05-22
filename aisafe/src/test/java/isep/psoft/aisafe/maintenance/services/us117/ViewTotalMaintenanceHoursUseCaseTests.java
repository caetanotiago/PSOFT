package isep.psoft.aisafe.maintenance.services.us117;

import isep.psoft.aisafe.maintenance.dto.TotalMaintenanceHoursDto;
import isep.psoft.aisafe.maintenance.repositories.MaintenanceRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ViewTotalMaintenanceHoursUseCaseTests {

    @Mock
    private MaintenanceRecordRepository repository;

    @InjectMocks
    private ViewTotalMaintenanceHoursUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenRepositoryReturnsMinutes_shouldConvertAndReturnHours() {
        // Arrange
        long totalMinutes = 150L;
        when(repository.sumTotalExpectedDurationMinutes()).thenReturn(totalMinutes);

        // Act
        TotalMaintenanceHoursDto result = useCase.execute();

        // Assert
        assertThat(result.getTotalHours()).isEqualTo(2.5);
        verify(repository, times(1)).sumTotalExpectedDurationMinutes();
    }

    @Test
    void whenRepositoryReturnsZeroMinutes_shouldReturnZeroHours() {
        // Arrange
        long totalMinutes = 0L;
        when(repository.sumTotalExpectedDurationMinutes()).thenReturn(totalMinutes);

        // Act
        TotalMaintenanceHoursDto result = useCase.execute();

        // Assert
        assertThat(result.getTotalHours()).isEqualTo(0.0);
        verify(repository, times(1)).sumTotalExpectedDurationMinutes();
    }
}
