package isep.psoft.aisafe.maintenance.services.us222;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MaintenanceScheduler {

    private final AlertService alertService;


    @Scheduled(cron = "0 0 0 * * ?")
    public void runMaintenanceCheck() {
        System.out.println("[SCHEDULER] A verificar as necessidades de manutenção da frota...");
        alertService.checkFleetMaintenance();
    }
}