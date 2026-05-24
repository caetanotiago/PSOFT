package isep.psoft.aisafe.maintenance.services.us119;

import isep.psoft.aisafe.maintenance.assemblers.MaintenanceRecordAssembler;
import isep.psoft.aisafe.maintenance.domain.MaintenanceRecord;
import isep.psoft.aisafe.maintenance.dto.CompleteRecordInputDto;
import isep.psoft.aisafe.maintenance.dto.MaintenanceRecordOutputDto;
import isep.psoft.aisafe.maintenance.repositories.MaintenanceRecordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

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
        // Arrange
        long recordId = 1L;
        long version = 0L;
        CompleteRecordInputDto dto = new CompleteRecordInputDto("All tasks done.");
        
        MaintenanceRecord mockRecord = mock(MaintenanceRecord.class);
        MaintenanceRecordOutputDto mockOutputDto = mock(MaintenanceRecordOutputDto.class);

        when(repository.findById(recordId)).thenReturn(Optional.of(mockRecord));
        when(mockRecord.getVersion()).thenReturn(version);
        
        // A SIMULAÇÃO QUE FALTAVA PARA O SAVE
        when(repository.save(any(MaintenanceRecord.class))).thenReturn(mockRecord);
        
        // A SIMULAÇÃO PARA O ASSEMBLER
        when(assembler.toModel(any(MaintenanceRecord.class))).thenReturn(mockOutputDto);
        
        // Act
        useCase.execute(recordId, dto, "\"" + version + "\"");

        // Assert
        verify(repository, times(1)).findById(recordId);
        verify(mockRecord, times(1)).complete(any());
        verify(repository, times(1)).save(mockRecord);
        verify(assembler, times(1)).toModel(mockRecord);
    }
}
