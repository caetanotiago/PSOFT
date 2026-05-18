package isep.psoft.aisafe;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
// (Importar os teus Repositories quando os tiveres)

@Component
public class Bootstrapper implements CommandLineRunner {

    // injetar os Repositories (ex: UserRepository, AircraftModelRepository)
    // @Autowired
    // private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("A iniciar o Bootstrapping de dados...");

        // 1. Criar Administradores do Sistema
        // Se a BD estiver vazia, cria o User "admin" com a password "admin"

        // 2. Pre-load de Aircraft Manufacturers (ex: Boeing, Airbus)

        // 3. Pre-load de Maintenance Templates

        System.out.println("Bootstrapping concluído com sucesso!");
    }
}