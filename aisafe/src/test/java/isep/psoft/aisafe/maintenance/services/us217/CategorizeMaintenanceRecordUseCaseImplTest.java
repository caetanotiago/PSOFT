package isep.psoft.aisafe.maintenance.services.us217;

import isep.psoft.aisafe.maintenance.assemblers.MaintenanceRecordAssembler;
import isep.psoft.aisafe.maintenance.domain.MaintenanceRecord;
import isep.psoft.aisafe.maintenance.domain.MaintenanceRecordNotFoundException;
import isep.psoft.aisafe.maintenance.repositories.MaintenanceRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategorizeMaintenanceRecordUseCaseImplTest {

    @Mock
    private MaintenanceRecordRepository recordRepository;

    // AQUI ESTÁ A CORREÇÃO: Damos um "Fantoche" do Assembler ao teste
    @Mock
    private MaintenanceRecordAssembler assembler;

    @InjectMocks
    private CategorizeMaintenanceRecordUseCaseImpl useCase;

    @Mock
    private MaintenanceRecord mockRecord;

    @BeforeEach
    void setUp() {
    }

    @Test
    void whenCategorizingWithValidData_shouldSucceed() {
        Long recordId = 1L;
        String category = "ENGINE";
        Long version = 0L;

        when(mockRecord.getVersion()).thenReturn(0L);
        when(recordRepository.findById(recordId)).thenReturn(Optional.of(mockRecord));
        when(recordRepository.save(any(MaintenanceRecord.class))).thenReturn(mockRecord);

        // Executamos o Use Case
        useCase.execute(recordId, category, version);

        // Validamos que a bd guardou
        verify(recordRepository, times(1)).save(mockRecord);
        // Validamos que o assembler também foi chamado corretamente!
        verify(assembler, times(1)).toModel(mockRecord);
    }

    @Test
    void whenRecordNotFound_shouldThrowException() {
        Long recordId = 999L;

        when(recordRepository.findById(recordId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(MaintenanceRecordNotFoundException.class, () ->
                useCase.execute(recordId, "ENGINE", 0L)
        );

        assertTrue(exception.getMessage().toLowerCase().contains("not found") ||
                exception.getMessage().toLowerCase().contains("encontrad"));
    }
}