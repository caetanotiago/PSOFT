package isep.psoft.aisafe;

import isep.psoft.aisafe.aircraftmanagement.domain.*;
import isep.psoft.aisafe.aircraftmanagement.repositories.AircraftModelRepository;
import isep.psoft.aisafe.aircraftmanagement.repositories.AircraftRepository;
import isep.psoft.aisafe.domain.user.Role;
import isep.psoft.aisafe.domain.user.SystemUser;
import isep.psoft.aisafe.domain.user.SystemUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Component
public class Bootstrapper implements CommandLineRunner {

    private final SystemUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    // 1. Declarar os repositórios aqui
    private final AircraftModelRepository aircraftModelRepository;
    private final AircraftRepository aircraftRepository;

    // 2. Adicionar os repositórios ao construtor para injeção automática
    public Bootstrapper(SystemUserRepository userRepository, 
                        PasswordEncoder passwordEncoder,
                        AircraftModelRepository aircraftModelRepository,
                        AircraftRepository aircraftRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.aircraftModelRepository = aircraftModelRepository;
        this.aircraftRepository = aircraftRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("A iniciar o Bootstrapping de dados...");

        // Criar Administrador
        if (userRepository.findByUsername("admin").isEmpty()) {
            userRepository.save(new SystemUser("admin", passwordEncoder.encode("admin123"), Role.ADMIN));
            System.out.println("Utilizador 'admin' criado com sucesso.");
        }

        // Criar Utilizador ATCC (utilizador principal)
        if (userRepository.findByUsername("atcc").isEmpty()) {
            userRepository.save(new SystemUser("atcc", passwordEncoder.encode("atcc123"), Role.ATCC));
            System.out.println("Utilizador 'atcc' criado com sucesso.");
        }

        // Criar Utilizador Backoffice Operator (WP#1A e WP#2A)
        if (userRepository.findByUsername("backoffice").isEmpty()) {
            userRepository.save(new SystemUser("backoffice", passwordEncoder.encode("bo123"), Role.BACKOFFICE_OPERATOR));
            System.out.println("Utilizador 'backoffice' criado com sucesso.");
        }

        // Criar Supervisor e Técnico de Manutenção (WP#4A)
        if (userRepository.findByUsername("supervisor").isEmpty()) {
            userRepository.save(new SystemUser("supervisor", passwordEncoder.encode("sup123"), Role.MAINTENANCE_SUPERVISOR));
            System.out.println("Utilizador 'supervisor' criado com sucesso.");
        }
        if (userRepository.findByUsername("technician").isEmpty()) {
            userRepository.save(new SystemUser("technician", passwordEncoder.encode("tech123"), Role.MAINTENANCE_TECHNICIAN));
            System.out.println("Utilizador 'technician' criado com sucesso.");
        }

        if (aircraftModelRepository.count() == 0) {
            AircraftModel boeing = new AircraftModel(
                    new ModelDesignation("Boeing", "737-800"),
                    new ModelSpecifications(180, 26000.0, 5765.0, 842.0)
            );
            aircraftModelRepository.save(boeing);

            AircraftModel airbus = new AircraftModel(
                    new ModelDesignation("Airbus", "A320"),
                    new ModelSpecifications(150, 24000.0, 6100.0, 840.0)
            );
            aircraftModelRepository.save(airbus);
            System.out.println("WP#1A: Aircraft Models injetados.");
        }

        // B. Injetar Aircrafts
        if (aircraftRepository.count() == 0) {
            var models = aircraftModelRepository.findAll();
            
            if (!models.isEmpty()) {
                AircraftModel model = models.get(0);
                
                Aircraft aircraft = new Aircraft(
                        new RegistrationNumber("CS-ABC"),
                        model,
                        new ManufacturingDate(LocalDate.of(2020, 1, 15)),
                        new SeatingCapacity(180),
                        new AircraftStatus("ACTIVE")
                );
                aircraftRepository.save(aircraft);
                System.out.println("WP#1A: Aircraft 'CS-ABC' injetado.");
            }
        }

        System.out.println("Bootstrapping concluído com sucesso!");
    

    }
}