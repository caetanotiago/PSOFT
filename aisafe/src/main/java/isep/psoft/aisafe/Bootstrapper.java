package isep.psoft.aisafe;

import isep.psoft.aisafe.aircraftmanagement.domain.*;
import isep.psoft.aisafe.aircraftmanagement.repositories.AircraftModelRepository;
import isep.psoft.aisafe.aircraftmanagement.repositories.AircraftRepository;
import isep.psoft.aisafe.airports.domain.*;
import isep.psoft.aisafe.airports.repositories.AirportRepository;
import isep.psoft.aisafe.domain.user.Role;
import isep.psoft.aisafe.domain.user.SystemUser;
import isep.psoft.aisafe.domain.user.SystemUserRepository;
import isep.psoft.aisafe.maintenance.domain.MaintenanceInterval;
import isep.psoft.aisafe.maintenance.domain.MaintenanceTemplate;
import isep.psoft.aisafe.maintenance.domain.TemplateType;
import isep.psoft.aisafe.maintenance.repositories.MaintenanceTemplateRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
public class Bootstrapper implements CommandLineRunner {

    private final SystemUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AircraftModelRepository aircraftModelRepository;
    private final AircraftRepository aircraftRepository;
    private final AirportRepository airportRepository;
    private final MaintenanceTemplateRepository maintenanceTemplateRepository;

    public Bootstrapper(SystemUserRepository userRepository,
                        PasswordEncoder passwordEncoder,
                        AircraftModelRepository aircraftModelRepository,
                        AircraftRepository aircraftRepository,
                        AirportRepository airportRepository,
                        MaintenanceTemplateRepository maintenanceTemplateRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.aircraftModelRepository = aircraftModelRepository;
        this.aircraftRepository = aircraftRepository;
        this.airportRepository = airportRepository;
        this.maintenanceTemplateRepository = maintenanceTemplateRepository;
    }

    @Override
    @Transactional
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

        // Criar Utilizador Backoffice Operator
        if (userRepository.findByUsername("backoffice").isEmpty()) {
            userRepository.save(new SystemUser("backoffice", passwordEncoder.encode("bo123"), Role.BACKOFFICE_OPERATOR));
            System.out.println("Utilizador 'backoffice' criado com sucesso.");
        }

        // Criar Supervisor e Técnico de Manutenção
        if (userRepository.findByUsername("supervisor").isEmpty()) {
            userRepository.save(new SystemUser("supervisor", passwordEncoder.encode("sup123"), Role.MAINTENANCE_SUPERVISOR));
            System.out.println("Utilizador 'supervisor' criado com sucesso.");
        }
        if (userRepository.findByUsername("technician").isEmpty()) {
            userRepository.save(new SystemUser("technician", passwordEncoder.encode("tech123"), Role.MAINTENANCE_TECHNICIAN));
            System.out.println("Utilizador 'technician' criado com sucesso.");
        }

        // Aircrafts (WP#1A)
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

        // Aeronaves Físicas - Injetar Avião para Manutenção
        if (aircraftRepository.count() == 0) {
            AircraftModel modeloBase = aircraftModelRepository.findAll().iterator().next();

            // CORREÇÃO 1: Adicionado o 5º argumento (AircraftStatus)
            Aircraft aircraft = new Aircraft(
                    new RegistrationNumber("CS-TVA"),
                    modeloBase,
                    new ManufacturingDate(LocalDate.of(2020, 1, 1)),
                    new SeatingCapacity(150),
                    new AircraftStatus("ACTIVE") // Assumindo que o construtor recebe uma String
            );
            aircraftRepository.save(aircraft);
            System.out.println("WP#1A: Aeronave 'CS-TVA' injetada.");
        }

        // Aeroportos (WP#2A)
        if (airportRepository.count() == 0) {
            airportRepository.save(new Airport(
                new IATACode("LIS"),
                new AirportDetails("Humberto Delgado", "Lisboa", "Portugal", "Europa", "Europe/Lisbon",
                    new Coordinates(38.7756, -9.1354)),
                AirportState.OPERATIONAL,
                List.of(new Runway("03", 3805.0, "N"), new Runway("21", 3805.0, "S"))
            ));
            airportRepository.save(new Airport(
                new IATACode("OPO"),
                new AirportDetails("Francisco Sá Carneiro", "Porto", "Portugal", "Europa", "Europe/Lisbon",
                    new Coordinates(41.2481, -8.6814)),
                AirportState.OPERATIONAL,
                List.of(new Runway("17", 3480.0, "N"), new Runway("35", 3480.0, "S"))
            ));
            airportRepository.save(new Airport(
                new IATACode("FAO"),
                new AirportDetails("Faro", "Faro", "Portugal", "Europa", "Europe/Lisbon",
                    new Coordinates(37.0144, -7.9659)),
                AirportState.OPERATIONAL,
                List.of(new Runway("10", 2490.0, "E"))
            ));
            airportRepository.save(new Airport(
                new IATACode("MAD"),
                new AirportDetails("Adolfo Suárez Madrid-Barajas", "Madrid", "Espanha", "Europa", "Europe/Madrid",
                    new Coordinates(40.4936, -3.5668)),
                AirportState.OPERATIONAL,
                List.of(new Runway("18L", 4350.0, "N"), new Runway("32R", 4100.0, "NW"))
            ));
            System.out.println("WP#2A: Aeroportos LIS, OPO, FAO, MAD injetados.");
        }

        // Manutenção (WP#4A) - A TUA PARTE!
        if (maintenanceTemplateRepository.count() == 0) {
            AircraftModel modeloBase = aircraftModelRepository.findAll().iterator().next();

            // CORREÇÃO 2: Mudado de ROUTINE para INSPECTION
            MaintenanceTemplate template = new MaintenanceTemplate(
                    "Annual Inspection",
                    TemplateType.INSPECTION,
                    new MaintenanceInterval(500, 365),
                    List.of("Check Engines", "Check Landing Gear", "Update Flight Software"),
                    List.of(modeloBase)
            );
            maintenanceTemplateRepository.save(template);
            System.out.println("WP#4A: Maintenance Templates injetados.");
        }

        System.out.println("✅ Bootstrapping concluído com sucesso!");
    }
}