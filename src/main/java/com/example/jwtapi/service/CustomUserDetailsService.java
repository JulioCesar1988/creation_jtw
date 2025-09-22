package com.example.jwtapi.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    /**
     * Carga un usuario por nombre de usuario
     * En un caso real, esto consultaría la base de datos
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Por simplicidad, creamos un usuario de ejemplo
        // En un caso real, esto consultaría la base de datos
        if ("admin".equals(username)) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            return org.springframework.security.core.userdetails.User.builder()
                    .username("admin")
                    .password(encoder.encode("admin123"))
                    .authorities(new ArrayList<>())
                    .build();
        }
        
        throw new UsernameNotFoundException("Usuario no encontrado: " + username);
    }
}
