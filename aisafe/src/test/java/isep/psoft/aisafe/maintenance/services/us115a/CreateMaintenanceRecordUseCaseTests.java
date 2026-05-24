package isep.psoft.aisafe.maintenance.services.us115a;

import isep.psoft.aisafe.aircraftmanagement.domain.RegistrationNumber;
import isep.psoft.aisafe.aircraftmanagement.repositories.AircraftRepository;
import isep.psoft.aisafe.maintenance.assemblers.MaintenanceRecordAssembler;
import isep.psoft.aisafe.maintenance.domain.MaintenanceRecord;
import isep.psoft.aisafe.maintenance.dto.CreateRecordDTO;
import isep.psoft.aisafe.maintenance.dto.MaintenanceRecordOutputDto;
import isep.psoft.aisafe.maintenance.repositories.MaintenanceRecordRepository;
import isep.psoft.aisafe.maintenance.repositories.MaintenanceTemplateRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateMaintenanceRecordUseCaseTests {

    @Mock
    private MaintenanceRecordRepository recordRepository;

    @Mock
    private MaintenanceTemplateRepository templateRepository;

    @Mock
    private AircraftRepository aircraftRepository;

    @Mock
    private MaintenanceRecordAssembler assembler;

    @InjectMocks
    private CreateMaintenanceRecordUseCase useCase;

    @Test
    void whenAllDataIsValid_shouldCreateAndSaveRecord() {

        CreateRecordDTO dto = new CreateRecordDTO("CS-TWB", 1L, "Annual inspection", LocalDate.now(), 480, "ENGINE");
        MaintenanceRecord savedRecord = mock(MaintenanceRecord.class);
        MaintenanceRecordOutputDto outputDto = mock(MaintenanceRecordOutputDto.class);

        when(aircraftRepository.existsByRegistrationNumber(any(RegistrationNumber.class))).thenReturn(true);
        when(templateRepository.existsById(dto.getTemplateId())).thenReturn(true);
        when(recordRepository.save(any(MaintenanceRecord.class))).thenReturn(savedRecord);
        when(assembler.toModel(savedRecord)).thenReturn(outputDto);

        MaintenanceRecordOutputDto result = useCase.execute(dto);

        assertThat(result).isEqualTo(outputDto);
        verify(recordRepository, times(1)).save(any(MaintenanceRecord.class));
    }

    @Test
    void whenTemplateDoesNotExist_shouldThrowException() {

        CreateRecordDTO dto = new CreateRecordDTO("CS-TWB", 99L, "Non-existent template test", LocalDate.now(), 60, "AVIONICS");
        
        when(aircraftRepository.existsByRegistrationNumber(any(RegistrationNumber.class))).thenReturn(true);
        when(templateRepository.existsById(dto.getTemplateId())).thenReturn(false);

        assertThatThrownBy(() -> useCase.execute(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Maintenance Template with ID 99 does not exist.");

        verify(recordRepository, never()).save(any());
    }

    @Test
    void whenAircraftDoesNotExist_shouldThrowException() {

        CreateRecordDTO dto = new CreateRecordDTO("CS-XXX", 1L, "Non-existent aircraft test", LocalDate.now(), 60, "STRUCTURE");

        when(aircraftRepository.existsByRegistrationNumber(any(RegistrationNumber.class))).thenReturn(false);

        assertThatThrownBy(() -> useCase.execute(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Aircraft with registration CS-XXX does not exist.");

        verify(templateRepository, never()).existsById(any());
        verify(recordRepository, never()).save(any());
    }
}
