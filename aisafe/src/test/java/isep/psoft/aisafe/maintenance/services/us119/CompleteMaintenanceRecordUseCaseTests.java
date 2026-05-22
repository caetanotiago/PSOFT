package isep.psoft.aisafe.maintenance.services.us119;

import isep.psoft.aisafe.maintenance.assemblers.MaintenanceRecordAssembler;
import isep.psoft.aisafe.maintenance.domain.MaintenanceRecord;
import isep.psoft.aisafe.maintenance.dto.CompleteRecordInputDto;
import isep.psoft.aisafe.maintenance.dto.MaintenanceRecordOutputDto;
import isep.psoft.aisafe.maintenance.repositories.MaintenanceRecordRepository;
import jakarta.persistence.OptimisticLockException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CompleteMaintenanceRecordUseCaseTests {

    @Mock
    private MaintenanceRecordRepository repository;

    @Mock
    private MaintenanceRecordAssembler assembler;

    @InjectMocks
    private CompleteMaintenanceRecordUseCase useCase;

    private MaintenanceRecord record;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        record = mock(MaintenanceRecord.class);
    }

    @Test
    void whenVersionMatches_shouldCompleteAndSaveRecord() {
        // Arrange
        long recordId = 1L;
        long version = 0L;
        CompleteRecordInputDto dto = new CompleteRecordInputDto("All tasks done.");
        MaintenanceRecordOutputDto outputDto = mock(MaintenanceRecordOutputDto.class);

        when(repository.findById(recordId)).thenReturn(Optional.of(record));
        when(record.getVersion()).thenReturn(version);
        when(repository.save(record)).thenReturn(record);
        when(assembler.toModel(record)).thenReturn(outputDto);

        // Act
        MaintenanceRecordOutputDto result = useCase.execute(recordId, dto, "\"" + version + "\"");

        // Assert
        verify(record, times(1)).complete(any());
        verify(repository, times(1)).save(record);
        assertThat(result).isEqualTo(outputDto);
    }

    @Test
    void whenVersionMismatches_shouldThrowOptimisticLockException() {
        // Arrange
        long recordId = 1L;
        long dbVersion = 1L;
        long clientVersion = 0L;
        CompleteRecordInputDto dto = new CompleteRecordInputDto("Notes from a stale client.");

        when(repository.findById(recordId)).thenReturn(Optional.of(record));
        when(record.getVersion()).thenReturn(dbVersion);

        // Act & Assert
        assertThatThrownBy(() -> useCase.execute(recordId, dto, "\"" + clientVersion + "\""))
                .isInstanceOf(OptimisticLockException.class)
                .hasMessage("The resource was modified by another user. Please refresh and try again.");

        verify(repository, never()).save(any());
    }

    @Test
    void whenRecordNotFound_shouldThrowRuntimeException() {
        // Arrange
        long recordId = 99L;
        when(repository.findById(recordId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> useCase.execute(recordId, new CompleteRecordInputDto("..."), "\"0\""))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Maintenance Record not found");
    }

    @Test
    void whenIfMatchHeaderIsMissing_shouldThrowIllegalArgumentException() {
        // Arrange
        long recordId = 1L;

        // Act & Assert
        assertThatThrownBy(() -> useCase.execute(recordId, new CompleteRecordInputDto("..."), null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("ETag version is required via If-Match header.");
    }
    
    @Test
    void whenRecordIsAlreadyCompleted_shouldThrowIllegalStateException() {
        // Arrange
        long recordId = 1L;
        long version = 0L;
        CompleteRecordInputDto dto = new CompleteRecordInputDto("Trying to complete again.");

        when(repository.findById(recordId)).thenReturn(Optional.of(record));
        when(record.getVersion()).thenReturn(version);
        doThrow(new IllegalStateException("This maintenance record has already been completed."))
                .when(record).complete(any());

        // Act & Assert
        assertThatThrownBy(() -> useCase.execute(recordId, dto, "\"" + version + "\""))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("This maintenance record has already been completed.");
    }
}
