package isep.psoft.aisafe.maintenance.services.us119;

import isep.psoft.aisafe.maintenance.assemblers.MaintenanceRecordAssembler;
import isep.psoft.aisafe.maintenance.domain.MaintenanceRecord;
import isep.psoft.aisafe.maintenance.dto.CompleteRecordInputDto;
import isep.psoft.aisafe.maintenance.dto.MaintenanceRecordOutputDto;
import isep.psoft.aisafe.maintenance.repositories.MaintenanceRecordRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompleteMaintenanceRecordUseCaseTests {

    @Mock
    private MaintenanceRecordRepository repository;

    @Mock
    private MaintenanceRecordAssembler assembler;

    @InjectMocks
    private CompleteMaintenanceRecordUseCase useCase;

    @Test
    void whenVersionMatches_shouldAttemptToSave() {
        long recordId = 1L;
        long version = 0L;
        CompleteRecordInputDto dto = new CompleteRecordInputDto("All tasks done.");

        MaintenanceRecord mockRecord = mock(MaintenanceRecord.class);
        MaintenanceRecordOutputDto mockOutputDto = mock(MaintenanceRecordOutputDto.class);

        when(repository.findById(recordId)).thenReturn(Optional.of(mockRecord));
        when(mockRecord.getVersion()).thenReturn(version);
        when(repository.save(any(MaintenanceRecord.class))).thenReturn(mockRecord);
        when(assembler.toModel(any(MaintenanceRecord.class))).thenReturn(mockOutputDto);

        useCase.execute(recordId, dto, "\"" + version + "\"");

        verify(repository, times(1)).findById(recordId);
        verify(mockRecord, times(1)).complete(any());
        verify(repository, times(1)).save(mockRecord);
        verify(assembler, times(1)).toModel(mockRecord);
    }

    @Test
    void whenRecordNotFound_shouldThrowEntityNotFoundException() {
        CompleteRecordInputDto dto = new CompleteRecordInputDto("All done");

        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(99L, dto, "\"1\""))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Maintenance Record not found with id: 99");

        verify(repository, never()).save(any());
    }

    @Test
    void whenVersionMismatches_shouldThrowOptimisticLockException() {
        CompleteRecordInputDto dto = new CompleteRecordInputDto("Completed");

        MaintenanceRecord mockRecord = mock(MaintenanceRecord.class);
        when(repository.findById(1L)).thenReturn(Optional.of(mockRecord));
        when(mockRecord.getVersion()).thenReturn(2L); // Versão na BD é 2

        assertThatThrownBy(() -> useCase.execute(1L, dto, "\"1\""))
                .isInstanceOf(OptimisticLockException.class)
                .hasMessageContaining("The resource was modified by another user");

        verify(mockRecord, never()).complete(any());
        verify(repository, never()).save(any());
    }
}