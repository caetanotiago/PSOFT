package isep.psoft.aisafe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "isep.psoft.aisafe")
public class AisafeApplication {

	public static void main(String[] args) {
		SpringApplication.run(AisafeApplication.class, args);
	}

}
