package isep.psoft.aisafe.maintenance.services.common;

import isep.psoft.aisafe.maintenance.assemblers.MaintenanceRecordAssembler;
import isep.psoft.aisafe.maintenance.domain.MaintenanceRecord;
import isep.psoft.aisafe.maintenance.dto.MaintenanceRecordOutputDto;
import isep.psoft.aisafe.maintenance.repositories.MaintenanceRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ViewMaintenanceRecordByIdUseCaseTests {

    @Mock
    private MaintenanceRecordRepository repository;

    @Mock
    private MaintenanceRecordAssembler assembler;

    @InjectMocks
    private ViewMaintenanceRecordByIdUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenRecordExists_shouldReturnDto() {

        long recordId = 1L;
        MaintenanceRecord record = mock(MaintenanceRecord.class);
        MaintenanceRecordOutputDto dto = mock(MaintenanceRecordOutputDto.class);

        when(repository.findById(recordId)).thenReturn(Optional.of(record));
        when(assembler.toModel(record)).thenReturn(dto);

        Optional<MaintenanceRecordOutputDto> result = useCase.execute(recordId);

        assertThat(result).isPresent().contains(dto);
        verify(repository, times(1)).findById(recordId);
        verify(assembler, times(1)).toModel(record);
    }

    @Test
    void whenRecordDoesNotExist_shouldReturnEmptyOptional() {

        long recordId = 99L;
        when(repository.findById(recordId)).thenReturn(Optional.empty());

        Optional<MaintenanceRecordOutputDto> result = useCase.execute(recordId);

        assertThat(result).isNotPresent();
        verify(repository, times(1)).findById(recordId);
        verify(assembler, never()).toModel(any());
    }
}
