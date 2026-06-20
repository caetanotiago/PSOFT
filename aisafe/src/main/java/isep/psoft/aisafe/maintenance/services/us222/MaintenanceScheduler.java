package isep.psoft.aisafe.maintenance.services.us222;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MaintenanceScheduler {

    private final AlertService alertService;

    // A expressão cron "0 0 0 * * ?" significa: "Às 00:00:00 de todos os dias"
    // Dica de Mestre: Se quiseres ver isto a funcionar agora mesmo para testares,
    // comenta a linha de baixo e usa esta: @Scheduled(cron = "0 * * * * ?") -> Corre a cada 1 minuto!

    @Scheduled(cron = "0 0 0 * * ?")
    public void runMaintenanceCheck() {
        System.out.println("[SCHEDULER] A verificar as necessidades de manutenção da frota...");
        alertService.checkFleetMaintenance();
    }
}