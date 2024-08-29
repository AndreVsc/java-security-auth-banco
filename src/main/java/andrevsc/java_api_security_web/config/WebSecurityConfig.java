package andrevsc.java_api_security_web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    @Autowired
    private SecurityDatabaseService securityDatabaseService;

    @Autowired
    public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(securityDatabaseService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())  // Disable CSRF for development purposes
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(HttpMethod.GET, "/users").hasRole("ADMIN")  // Only ADMIN can access the GET /users endpoint
                .requestMatchers(HttpMethod.GET, "/users/{id}").hasAnyRole("ADMIN", "USER")  // ADMIN and USER can access GET /users/{id}
                .requestMatchers(HttpMethod.POST, "/users").hasRole("ADMIN")  // Only ADMIN can create a user
                .requestMatchers(HttpMethod.PUT, "/users/{id}").hasRole("ADMIN")  // Only ADMIN can update a user
                .requestMatchers(HttpMethod.DELETE, "/users/{id}").hasRole("ADMIN")  // Only ADMIN can delete a user
                .requestMatchers("/welcome/user").hasAnyRole("USER", "ADMIN")  // USER and ADMIN can access /welcome/user
                .requestMatchers("/welcome/admin").hasRole("ADMIN")  // Only ADMIN can access /welcome/admin
                .requestMatchers("/welcome/all").permitAll()  // Anyone can access /welcome/all
                .anyRequest().authenticated()  // All other requests need authentication
            )
            .httpBasic(Customizer.withDefaults())  // Enable HTTP Basic authentication with defaults
            .formLogin(form -> form
                .permitAll()  // Use default login page
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );

        return http.build();
    }
}
