package isep.psoft.aisafe.maintenance.services.us218;

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
class SearchMaintenanceRecordsUseCaseImplTest {

    @Mock
    private MaintenanceRecordRepository recordRepository;

    @Mock
    private MaintenanceRecordAssembler assembler;

    @InjectMocks
    private SearchMaintenanceRecordsUseCaseImpl useCase;

    @Mock
    private MaintenanceRecord mockRecord;

    @Mock
    private MaintenanceRecordOutputDto mockDto;

    @BeforeEach
    void setUp() {
    }

    @Test
    void whenSearchingWithFilters_shouldReturnMatchingRecords() {
        String status = "COMPLETED";
        String component = "EXTERIOR";

        when(recordRepository.searchRecords(any(), any(), any())).thenReturn(List.of(mockRecord));
        when(assembler.toModel(mockRecord)).thenReturn(mockDto);

        Iterable<MaintenanceRecordOutputDto> result = useCase.execute(status, component, null);

        assertNotNull(result);
        assertTrue(result.iterator().hasNext());
        verify(assembler, times(1)).toModel(mockRecord);
    }

    @Test
    void whenSearchingWithNoMatches_shouldReturnEmptyList() {
        String status = "ONGOING";
        String component = "INTERIOR";

        when(recordRepository.searchRecords(any(), any(), any())).thenReturn(List.of());

        Iterable<MaintenanceRecordOutputDto> result = useCase.execute(status, component, null);

        assertNotNull(result);
        assertFalse(result.iterator().hasNext());

        verify(assembler, never()).toModel(any());
    }
}