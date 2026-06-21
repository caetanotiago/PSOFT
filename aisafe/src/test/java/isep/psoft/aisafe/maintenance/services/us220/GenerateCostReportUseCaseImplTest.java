package isep.psoft.aisafe.maintenance.services.us220;

import isep.psoft.aisafe.maintenance.dto.CostItemDto;
import isep.psoft.aisafe.maintenance.dto.MaintenanceCostReportDto;
import isep.psoft.aisafe.maintenance.repositories.MaintenanceRecordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenerateCostReportUseCaseImplTest {

    @Mock
    private MaintenanceRecordRepository repository;

    @InjectMocks
    private GenerateCostReportUseCaseImpl useCase;

    @Test
    void whenReportTypeIsAircraft_shouldReturnAircraftCosts() {
        // Arrange
        CostItemDto mockItem = mock(CostItemDto.class);
        when(repository.calculateCostsPerAircraft()).thenReturn(List.of(mockItem));

        // Act
        MaintenanceCostReportDto result = useCase.execute("AIRCRAFT");

        // Assert
        assertThat(result).isNotNull();


        verify(repository, times(1)).calculateCostsPerAircraft();
        verify(repository, never()).calculateCostsPerModel();
    }

    @Test
    void whenReportTypeIsModel_shouldReturnModelCosts() {
        // Arrange
        CostItemDto mockItem = mock(CostItemDto.class);
        when(repository.calculateCostsPerModel()).thenReturn(List.of(mockItem));

        MaintenanceCostReportDto result = useCase.execute("  MoDel  ");

        // Assert
        assertThat(result).isNotNull();
        verify(repository, times(1)).calculateCostsPerModel();
        verify(repository, never()).calculateCostsPerAircraft();
    }

    @Test
    void whenReportTypeIsNull_shouldThrowException() {
        // Assert
        assertThatThrownBy(() -> useCase.execute(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("O tipo de relatório (reportType) é obrigatório");
    }

    @Test
    void whenReportTypeIsEmpty_shouldThrowException() {
        // Assert
        assertThatThrownBy(() -> useCase.execute("   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("O tipo de relatório (reportType) é obrigatório");
    }

    @Test
    void whenReportTypeIsInvalid_shouldThrowException() {
        // Assert
        assertThatThrownBy(() -> useCase.execute("INVALID_TYPE"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Tipo de relatório inválido");
    }
}