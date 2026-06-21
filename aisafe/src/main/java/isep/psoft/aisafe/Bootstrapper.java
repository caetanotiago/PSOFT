package isep.psoft.aisafe;

import isep.psoft.aisafe.aircraftmanagement.domain.*;
import isep.psoft.aisafe.aircraftmanagement.repositories.AircraftModelRepository;
import isep.psoft.aisafe.aircraftmanagement.repositories.AircraftRepository;
import isep.psoft.aisafe.airports.domain.*;
import isep.psoft.aisafe.airports.repositories.AirportRepository;
import isep.psoft.aisafe.domain.user.Role;
import isep.psoft.aisafe.domain.user.SystemUser;
import isep.psoft.aisafe.domain.user.SystemUserRepository;
import isep.psoft.aisafe.flightroutes.domain.FlightRoute;
import isep.psoft.aisafe.flightroutes.domain.FlightSchedule;
import isep.psoft.aisafe.flightroutes.domain.ScheduledFlight;
import isep.psoft.aisafe.flightroutes.factories.FlightRouteFactory;
import isep.psoft.aisafe.flightroutes.repositories.FlightRouteRepository;
import isep.psoft.aisafe.flightroutes.repositories.ScheduledFlightRepository;
import isep.psoft.aisafe.maintenance.domain.*;
import isep.psoft.aisafe.maintenance.repositories.MaintenanceRecordRepository;
import isep.psoft.aisafe.maintenance.repositories.MaintenanceTemplateRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
public class Bootstrapper implements CommandLineRunner {

    private final SystemUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AircraftModelRepository aircraftModelRepository;
    private final AircraftRepository aircraftRepository;
    private final AirportRepository airportRepository;
    private final MaintenanceTemplateRepository maintenanceTemplateRepository;
    private final MaintenanceRecordRepository maintenanceRecordRepository; // NOVO
    private final FlightRouteRepository flightRouteRepository;
    private final ScheduledFlightRepository scheduledFlightRepository;
    private final FlightRouteFactory flightRouteFactory;

    public Bootstrapper(SystemUserRepository userRepository,
                        PasswordEncoder passwordEncoder,
                        AircraftModelRepository aircraftModelRepository,
                        AircraftRepository aircraftRepository,
                        AirportRepository airportRepository,
                        MaintenanceTemplateRepository maintenanceTemplateRepository,
                        MaintenanceRecordRepository maintenanceRecordRepository, // NOVO
                        FlightRouteRepository flightRouteRepository,
                        ScheduledFlightRepository scheduledFlightRepository,
                        FlightRouteFactory flightRouteFactory) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.aircraftModelRepository = aircraftModelRepository;
        this.aircraftRepository = aircraftRepository;
        this.airportRepository = airportRepository;
        this.maintenanceTemplateRepository = maintenanceTemplateRepository;
        this.maintenanceRecordRepository = maintenanceRecordRepository; // NOVO
        this.flightRouteRepository = flightRouteRepository;
        this.scheduledFlightRepository = scheduledFlightRepository;
        this.flightRouteFactory = flightRouteFactory;
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

        // Aeronaves Físicas
        if (aircraftRepository.count() == 0) {
            AircraftModel modeloBase = aircraftModelRepository.findAll().iterator().next();

            Aircraft aircraft1 = new Aircraft(
                    new RegistrationNumber("CS-TVA"),
                    modeloBase,
                    new ManufacturingDate(LocalDate.of(2020, 1, 1)),
                    new SeatingCapacity(150),
                    new AircraftStatus("AVAILABLE")
            );
            aircraftRepository.save(aircraft1);

            Aircraft aircraft2 = new Aircraft(
                    new RegistrationNumber("CS-TVB"),
                    modeloBase,
                    new ManufacturingDate(LocalDate.of(2022, 5, 10)),
                    new SeatingCapacity(150),
                    new AircraftStatus("AVAILABLE")
            );
            aircraftRepository.save(aircraft2);

            System.out.println("WP#1A: Aeronaves 'CS-TVA' e 'CS-TVB' injetadas.");
        }

        // Aeroportos (WP#2A)
        if (airportRepository.count() == 0) {
            airportRepository.save(new Airport(new IATACode("LIS"), new AirportDetails("Humberto Delgado", "Lisboa", "Portugal", "Europa", "Europe/Lisbon", new Coordinates(38.7756, -9.1354)), AirportState.OPERATIONAL, List.of(new Runway("03", 3805.0, "N"), new Runway("21", 3805.0, "S"))));
            airportRepository.save(new Airport(new IATACode("OPO"), new AirportDetails("Francisco Sá Carneiro", "Porto", "Portugal", "Europa", "Europe/Lisbon", new Coordinates(41.2481, -8.6814)), AirportState.OPERATIONAL, List.of(new Runway("17", 3480.0, "N"), new Runway("35", 3480.0, "S"))));
            airportRepository.save(new Airport(new IATACode("FAO"), new AirportDetails("Faro", "Faro", "Portugal", "Europa", "Europe/Lisbon", new Coordinates(37.0144, -7.9659)), AirportState.OPERATIONAL, List.of(new Runway("10", 2490.0, "E"))));
            airportRepository.save(new Airport(new IATACode("MAD"), new AirportDetails("Adolfo Suárez Madrid-Barajas", "Madrid", "Espanha", "Europa", "Europe/Madrid", new Coordinates(40.4936, -3.5668)), AirportState.OPERATIONAL, List.of(new Runway("18L", 4350.0, "N"), new Runway("32R", 4100.0, "NW"))));
            airportRepository.save(new Airport(new IATACode("BCN"), new AirportDetails("Josep Tarradellas Barcelona-El Prat", "Barcelona", "Espanha", "Europa", "Europe/Madrid", new Coordinates(41.2974, 2.0833)), AirportState.OPERATIONAL, List.of(new Runway("07L", 3352.0, "E"), new Runway("25R", 3352.0, "W"))));
            System.out.println("WP#2A: Aeroportos LIS, OPO, FAO, MAD, BCN injetados.");
        }

        // Manutenção (WP#4A) - Templates
        if (maintenanceTemplateRepository.count() == 0) {
            AircraftModel modeloBase = aircraftModelRepository.findAll().iterator().next();

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

        // NOVO: Manutenção (WP#4B) - Registo de Manutenções
        if (maintenanceRecordRepository.count() == 0) {
            MaintenanceTemplate template = maintenanceTemplateRepository.findAll().get(0);

            RecordDetails details1 = new RecordDetails("Inspecao de Rotina", LocalDate.now().plusDays(2), 180);

            // AQUI ESTÁ A CORREÇÃO: Com o componente adicionado para respeitar os 4 argumentos que o IDE pede!
            MaintenanceRecord record1 = new MaintenanceRecord(
                    "CS-TVB",
                    template.getId(),
                    details1,
                    new MaintenanceComponent("ENGINE")
            );

            maintenanceRecordRepository.save(record1);

            System.out.println("WP#4B: Maintenance Record (Ongoing) injetado com sucesso!");
        }

        // Rotas (WP#3A) + Voos Agendados (WP#3B)
        if (flightRouteRepository.count() == 0) {
            Airport lis = airportRepository.findById(new IATACode("LIS")).orElseThrow();
            Airport opo = airportRepository.findById(new IATACode("OPO")).orElseThrow();
            Airport mad = airportRepository.findById(new IATACode("MAD")).orElseThrow();
            Airport fao = airportRepository.findById(new IATACode("FAO")).orElseThrow();
            Airport bcn = airportRepository.findById(new IATACode("BCN")).orElseThrow();

            FlightRoute lisOpo = flightRouteRepository.save(flightRouteFactory.createRoute(lis, opo, 313.0, 1000.0, 100, 60));
            FlightRoute opoMad = flightRouteRepository.save(flightRouteFactory.createRoute(opo, mad, 421.0, 1000.0, 100, 75));
            FlightRoute lisFao = flightRouteRepository.save(flightRouteFactory.createRoute(lis, fao, 278.0, 1000.0, 100, 55));
            // Segunda alternativa para LIS→MAD (via FAO): US216 mostra logo 2 itinerários por default.
            FlightRoute faoMad = flightRouteRepository.save(flightRouteFactory.createRoute(fao, mad, 525.0, 1000.0, 100, 70));
            // Rota INATIVA: BCN só tem entrada por esta rota, logo LIS→BCN é IMPOSSÍVEL até ser ativada (demo US216/US112).
            FlightRoute madBcn = flightRouteFactory.createRoute(mad, bcn, 485.0, 1000.0, 100, 65);
            madBcn.changeStatus("INACTIVE");
            flightRouteRepository.save(madBcn);
            System.out.println("WP#3A: Rotas LIS-OPO, OPO-MAD, LIS-FAO, FAO-MAD (ativas) e MAD-BCN (INATIVA) injetadas.");

            if (scheduledFlightRepository.count() == 0) {
                String regA = "CS-TVA";
                scheduledFlightRepository.save(new ScheduledFlight(regA, lisOpo, new FlightSchedule(LocalDate.now().plusDays(1), LocalTime.of(10, 0))));
                scheduledFlightRepository.save(new ScheduledFlight(regA, lisOpo, new FlightSchedule(LocalDate.now().plusDays(2), LocalTime.of(14, 30))));
                scheduledFlightRepository.save(new ScheduledFlight(regA, opoMad, new FlightSchedule(LocalDate.now().plusDays(3), LocalTime.of(9, 15))));
                String regB = "CS-TVB";
                scheduledFlightRepository.save(new ScheduledFlight(regB, lisFao, new FlightSchedule(LocalDate.now().plusDays(4), LocalTime.of(8, 0))));
                scheduledFlightRepository.save(new ScheduledFlight(regB, faoMad, new FlightSchedule(LocalDate.now().plusDays(5), LocalTime.of(12, 0))));
                System.out.println("WP#3B: Voos agendados injetados (popularidade: LIS-OPO=2, OPO-MAD=1, LIS-FAO=1, FAO-MAD=1).");
            }
        }
        System.out.println("✅ Bootstrapping concluído com sucesso!");
    }
}