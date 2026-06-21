package isep.psoft.aisafe.maintenance.services.us219;

import isep.psoft.aisafe.maintenance.assemblers.MaintenanceRecordAssembler;
import isep.psoft.aisafe.maintenance.domain.MaintenanceRecord;
import isep.psoft.aisafe.maintenance.dto.MaintenanceRecordOutputDto;
import isep.psoft.aisafe.maintenance.repositories.MaintenanceRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ViewOngoingMaintenanceUseCaseImplTest {

    @Mock
    private MaintenanceRecordRepository repository;

    @Mock
    private MaintenanceRecordAssembler assembler;

    @InjectMocks
    private ViewOngoingMaintenanceUseCaseImpl useCase;

    @Mock
    private MaintenanceRecord mockRecord;

    @Mock
    private MaintenanceRecordOutputDto mockDto;

    @BeforeEach
    void setUp() {
    }

    @Test
    void whenViewingOngoingMaintenance_withRecords_shouldReturnList() {
        // CORRIGIDO AQUI: Colocámos o método exato que criaste no teu código
        when(repository.findByCompletionNotesIsNull()).thenReturn(List.of(mockRecord));
        when(assembler.toModel(mockRecord)).thenReturn(mockDto);

        // CORRIGIDO AQUI: A tua classe devolve uma List e não Iterable
        List<MaintenanceRecordOutputDto> result = useCase.execute();

        assertNotNull(result);
        assertFalse(result.isEmpty()); // A lista não pode vir vazia
        verify(assembler, times(1)).toModel(mockRecord);
    }

    @Test
    void whenViewingOngoingMaintenance_withNoRecords_shouldReturnEmptyList() {
        // Simula uma base de dados onde não há manutenções a decorrer (lista vazia)
        when(repository.findByCompletionNotesIsNull()).thenReturn(List.of());

        List<MaintenanceRecordOutputDto> result = useCase.execute();

        assertNotNull(result);
        assertTrue(result.isEmpty()); // A lista tem de vir vazia

        // Verifica que o assembler nunca tentou converter registos (porque não havia nenhum)
        verify(assembler, never()).toModel(any());
    }
}