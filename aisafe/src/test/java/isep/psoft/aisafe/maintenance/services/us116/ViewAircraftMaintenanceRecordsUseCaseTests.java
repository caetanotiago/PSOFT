package isep.psoft.aisafe.maintenance.services.us116;

import isep.psoft.aisafe.aircraftmanagement.domain.Aircraft;
import isep.psoft.aisafe.aircraftmanagement.repositories.AircraftRepository;
import isep.psoft.aisafe.maintenance.assemblers.MaintenanceRecordAssembler;
import isep.psoft.aisafe.maintenance.domain.MaintenanceRecord;
import isep.psoft.aisafe.maintenance.dto.MaintenanceRecordOutputDto;
import isep.psoft.aisafe.maintenance.repositories.MaintenanceRecordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ViewAircraftMaintenanceRecordsUseCaseTests {

    @Mock
    private MaintenanceRecordRepository repository;

    @Mock
    private MaintenanceRecordAssembler assembler;

    @Mock
    private AircraftRepository aircraftRepository;

    @InjectMocks
    private ViewAircraftMaintenanceRecordsUseCase useCase;

    @Test
    void whenAircraftHasRecords_shouldReturnDtoList() {
        String registration = "CS-TVA";
        MaintenanceRecord record1 = mock(MaintenanceRecord.class);
        MaintenanceRecordOutputDto dto1 = mock(MaintenanceRecordOutputDto.class);

        // Criamos um mock de um avião para a pesquisa não dar erro
        Aircraft mockAircraft = mock(Aircraft.class);

        // CORREÇÃO: Usamos o método da matrícula que retorna um Optional!
        when(aircraftRepository.findByRegistration_Registration(anyString())).thenReturn(Optional.of(mockAircraft));

        when(repository.findAllByAircraftRegistration(registration)).thenReturn(List.of(record1));
        when(assembler.toModel(record1)).thenReturn(dto1);

        List<MaintenanceRecordOutputDto> result = useCase.execute(registration);

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(dto1);
        verify(repository, times(1)).findAllByAircraftRegistration(registration);
        verify(assembler, times(1)).toModel(record1);
    }

    @Test
    void whenAircraftHasNoRecords_shouldReturnEmptyList() {
        String registration = "CS-TVB";
        Aircraft mockAircraft = mock(Aircraft.class);

        when(aircraftRepository.findByRegistration_Registration(anyString())).thenReturn(Optional.of(mockAircraft));
        when(repository.findAllByAircraftRegistration(registration)).thenReturn(Collections.emptyList());

        List<MaintenanceRecordOutputDto> result = useCase.execute(registration);

        assertThat(result).isEmpty();
        verify(repository, times(1)).findAllByAircraftRegistration(registration);
        verify(assembler, never()).toModel(any());
    }

    @Test
    void whenAircraftDoesNotExist_shouldThrowException() {
        String ghostRegistration = "CS-XYZ";

        // Simulamos que o avião não foi encontrado (devolve Optional vazio)
        when(aircraftRepository.findByRegistration_Registration(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(ghostRegistration))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Aircraft not found");

        verify(repository, never()).findAllByAircraftRegistration(anyString());
    }
}