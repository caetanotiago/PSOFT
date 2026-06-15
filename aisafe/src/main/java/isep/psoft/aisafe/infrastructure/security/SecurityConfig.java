package isep.psoft.aisafe.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**", "/h2-console/**").permitAll()
                        .requestMatchers("/v3/api-docs", "/v3/api-docs/**", "/v3/api-docs.yaml",
                                "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        
                        // Regras do WP#1A (Aircraft Models)
                        .requestMatchers(HttpMethod.POST,  "/api/aircraft-models").hasRole("BACKOFFICE_OPERATOR")
                        .requestMatchers(HttpMethod.GET,   "/api/aircraft-models/**").hasAnyRole("BACKOFFICE_OPERATOR", "ATCC")
                        .requestMatchers(HttpMethod.POST,  "/api/aircrafts").hasRole("ATCC")
                        .requestMatchers(HttpMethod.GET,   "/api/aircrafts/**").hasAnyRole("ATCC", "MAINTENANCE_SUPERVISOR", "MAINTENANCE_TECHNICIAN")

                        //Regras para o WP#2A (Aeroportos)
                        .requestMatchers(HttpMethod.POST,  "/airports").hasRole("BACKOFFICE_OPERATOR")
                        .requestMatchers(HttpMethod.GET,   "/airports").hasAnyRole("BACKOFFICE_OPERATOR", "ATCC")
                        .requestMatchers(HttpMethod.GET,   "/airports/*").hasAnyRole("BACKOFFICE_OPERATOR", "ATCC")
                        .requestMatchers(HttpMethod.POST,  "/airports/*/certifications").hasRole("BACKOFFICE_OPERATOR")
                        .requestMatchers(HttpMethod.PATCH, "/airports/*/status").hasRole("BACKOFFICE_OPERATOR")

                        //Regras para o WP#3A (FlightRoutes)
                        .requestMatchers(HttpMethod.POST,  "/api/routes").hasRole("ATCC")
                        .requestMatchers(HttpMethod.PATCH, "/api/routes/*").hasAnyRole("ATCC", "BACKOFFICE_OPERATOR")
                        .requestMatchers(HttpMethod.GET,   "/api/routes/**").hasRole("ATCC")

                        //Regras para o WP#3B (Scheduled Flights / Flight Operations)
                        .requestMatchers(HttpMethod.POST,  "/api/scheduled-flights").hasRole("ATCC")
                        .requestMatchers(HttpMethod.GET,   "/api/scheduled-flights/**").hasRole("ATCC")

                        // Regras do WP#4A (Maintenance Templates & Records)
                        .requestMatchers(HttpMethod.POST,  "/api/maintenance-templates").hasRole("BACKOFFICE_OPERATOR")
                        .requestMatchers(HttpMethod.GET,   "/api/maintenance-templates/**").hasAnyRole("BACKOFFICE_OPERATOR", "MAINTENANCE_SUPERVISOR")
                        .requestMatchers(HttpMethod.POST,  "/api/maintenance-records").hasAnyRole("MAINTENANCE_SUPERVISOR", "MAINTENANCE_TECHNICIAN")
                        .requestMatchers(HttpMethod.PATCH, "/api/maintenance-records/*").hasAnyRole("MAINTENANCE_SUPERVISOR", "MAINTENANCE_TECHNICIAN")
                        .requestMatchers(HttpMethod.GET,   "/api/maintenance-records/**").hasAnyRole("MAINTENANCE_SUPERVISOR", "MAINTENANCE_TECHNICIAN", "ATCC")
                        
                        .anyRequest().authenticated()
                )
                // Return 401 (not 403) when an unauthenticated request hits a protected endpoint.
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .headers(h -> h.frameOptions(f -> f.disable()))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
