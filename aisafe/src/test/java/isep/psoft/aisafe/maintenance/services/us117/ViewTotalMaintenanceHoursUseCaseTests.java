package isep.psoft.aisafe.maintenance.services.us117;

import isep.psoft.aisafe.maintenance.dto.TotalMaintenanceHoursDto;
import isep.psoft.aisafe.maintenance.repositories.MaintenanceRecordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ViewTotalMaintenanceHoursUseCaseTests {

    @Mock
    private MaintenanceRecordRepository repository;

    @InjectMocks
    private ViewTotalMaintenanceHoursUseCase useCase;

    @Test
    void whenRepositoryReturnsMinutes_shouldConvertAndReturnHours() {

        long totalMinutes = 150L;
        when(repository.sumTotalExpectedDurationMinutes()).thenReturn(totalMinutes);

        TotalMaintenanceHoursDto result = useCase.execute();

        assertThat(result.getTotalHours()).isEqualTo(2.5);
        verify(repository, times(1)).sumTotalExpectedDurationMinutes();
    }

    @Test
    void whenRepositoryReturnsZeroMinutes_shouldReturnZeroHours() {

        long totalMinutes = 0L;
        when(repository.sumTotalExpectedDurationMinutes()).thenReturn(totalMinutes);

        TotalMaintenanceHoursDto result = useCase.execute();

        assertThat(result.getTotalHours()).isEqualTo(0.0);
        verify(repository, times(1)).sumTotalExpectedDurationMinutes();
    }
}
