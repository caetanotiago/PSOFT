/*package isep.psoft.aisafe.aircraftmanagement.services;

import isep.psoft.aisafe.aircraftmanagement.domain.*;
import isep.psoft.aisafe.aircraftmanagement.repositories.AircraftRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateAircraftStatusServiceImplTest {

    @Mock
    private AircraftRepository aircraftRepository;

    @InjectMocks
    private UpdateAircraftStatusServiceImpl updateService;

    private Aircraft aircraft;
    private final String regNumber = "CS-ABC";

    @BeforeEach
    void setUp() {
        // Setup de um avião inicial com versão 0
        AircraftModel model = mock(AircraftModel.class);
        aircraft = new Aircraft(new RegistrationNumber(regNumber), model, 
                                new ManufacturingDate(LocalDate.now()), 
                                new SeatingCapacity(100), 
                                new AircraftStatus("ACTIVE"));
        
        // Simulação da versão inicial (Reflection ou mock seria necessário para setar versão em testes unitários puros, 
        // mas aqui focamos na lógica do service)
    }

    @Test
    void ensureValidStatusUpdateSucceeds() {
        when(aircraftRepository.findById(any(RegistrationNumber.class))).thenReturn(Optional.of(aircraft));
        when(aircraftRepository.save(any(Aircraft.class))).thenReturn(aircraft);

        Aircraft result = updateService.updateStatus(regNumber, "UNDER_MAINTENANCE", 0L);

        assertEquals("UNDER_MAINTENANCE", result.getStatus().getState());
        verify(aircraftRepository).save(aircraft);
    }

    @Test
    void ensureConcurrentUpdateThrowsOptimisticLockingException() {
        // Simula que o avião na BD tem versão 1, mas o utilizador enviou versão 0 (está desatualizado)
        when(aircraftRepository.findById(any(RegistrationNumber.class))).thenReturn(Optional.of(aircraft));
        
        // Versão esperada (0) é diferente da versão atual (mockada como 1)
        // Nota: Em testes de integração reais, isto testaria o @Version do JPA.
        // Aqui simulamos a lógica do service:
        assertThrows(ObjectOptimisticLockingFailureException.class, () -> {
            updateService.updateStatus(regNumber, "INACTIVE", 99L); 
        });
    }
}*/