package isep.psoft.aisafe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

// Nota: @EnableJpaRepositories explícito foi removido por ser redundante — o @SpringBootApplication
// já ativa os repositórios JPA (a partir deste pacote) via auto-configuração. Mantê-lo forçava o JPA
// mesmo em slices @WebMvcTest (sem datasource), quebrando os testes de controller.
@SpringBootApplication
@EnableScheduling
public class AisafeApplication {

	public static void main(String[] args) {
		SpringApplication.run(AisafeApplication.class, args);
	}

}
