package andrevsc.java_api_security_web.init;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import andrevsc.java_api_security_web.repository.UserRepository;
import andrevsc.java_api_security_web.model.User;

@Component
public class StartApplication implements CommandLineRunner {
    @Autowired
    private UserRepository repository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        User user = repository.findByUsername("ADMIN");
        if (user == null) {
            User newUser = new User();
            newUser.setUsername("ADMIN");
            newUser.setPassword(passwordEncoder.encode("ADMIN"));
            newUser.setRoles(Arrays.asList("ADMIN", "USER"));
            repository.save(newUser);
        }
        
        user = repository.findByUsername("USER");
        if (user == null) {
            User newUser = new User();
            newUser.setUsername("USER");
            newUser.setPassword(passwordEncoder.encode("USER"));
            newUser.setRoles(Arrays.asList("USER"));
            repository.save(newUser);
        }
    }
}
