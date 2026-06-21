package isep.psoft.aisafe.maintenance.services.us222;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MaintenanceSchedulerTest {

    @Mock
    private AlertService alertService;

    @InjectMocks
    private MaintenanceScheduler scheduler;

    @Test
    void executeScheduler_shouldCallCheckFleetMaintenance() {
        // Act
        scheduler.runMaintenanceCheck();

        // Assert
        verify(alertService, times(1)).checkFleetMaintenance();
    }
}