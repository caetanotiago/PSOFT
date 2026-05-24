package isep.psoft.aisafe;

import isep.psoft.aisafe.domain.user.Role;
import isep.psoft.aisafe.domain.user.SystemUser;
import isep.psoft.aisafe.domain.user.SystemUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class Bootstrapper implements CommandLineRunner {

    private final SystemUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Bootstrapper(SystemUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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

        // O pre-load dos Aircraft Manufacturers e Maintenance Templates 
        

        System.out.println("Bootstrapping concluído com sucesso!");
    }
}