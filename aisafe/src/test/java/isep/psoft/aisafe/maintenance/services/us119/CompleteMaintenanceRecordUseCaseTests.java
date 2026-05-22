package isep.psoft.aisafe.maintenance.services.us119;

import isep.psoft.aisafe.maintenance.assemblers.MaintenanceRecordAssembler;
import isep.psoft.aisafe.maintenance.domain.MaintenanceRecord;
import isep.psoft.aisafe.maintenance.dto.CompleteRecordInputDto;
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
        
        // Usando um MOCK simples em vez de um SPY para máxima simplicidade
        MaintenanceRecord mockRecord = mock(MaintenanceRecord.class);

        when(repository.findById(recordId)).thenReturn(Optional.of(mockRecord));
        when(mockRecord.getVersion()).thenReturn(version);
        
        // Act
        useCase.execute(recordId, dto, "\"" + version + "\"");

        // Assert
        // A verificação mais simples possível: o método save foi chamado?
        verify(repository, times(1)).save(mockRecord);
    }
}
