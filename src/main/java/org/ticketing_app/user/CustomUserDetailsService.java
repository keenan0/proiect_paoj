package org.ticketing_app.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.ticketing_app.model.User;

import java.util.Collections;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final CustomUserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(CustomUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsername(username);

        User user = userOptional.orElseThrow(() ->
                new UsernameNotFoundException("User not found with username: " + username)
        );

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                true, // account non-expired
                true, // credentials non-expired
                true, // account non-locked
                Collections.singleton(new SimpleGrantedAuthority(user.getRole()))
        );
    }
}