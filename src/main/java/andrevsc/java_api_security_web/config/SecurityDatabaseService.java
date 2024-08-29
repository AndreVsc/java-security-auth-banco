package andrevsc.java_api_security_web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import andrevsc.java_api_security_web.repository.UserRepository;
import andrevsc.java_api_security_web.model.User;

import java.util.HashSet;
import java.util.Set; 

@Service
public class SecurityDatabaseService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       User userEntity = userRepository.findByUsername(username);
       if (userEntity == null) {
           throw new UsernameNotFoundException("User not found");
       }
       Set<SimpleGrantedAuthority> authorities = new HashSet<>();
       userEntity.getRoles().forEach(role -> {
           authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
       });
       UserDetails user = new org.springframework.security.core.userdetails.User(userEntity.getUsername(), userEntity.getPassword(), authorities);
       return user;
    }
}