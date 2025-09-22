package com.example.jwtapi.service;

import com.example.jwtapi.dto.JwtResponse;
import com.example.jwtapi.dto.LoginRequest;
import com.example.jwtapi.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * Autentica un usuario y genera un token JWT
     */
    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetails);

        return new JwtResponse(token, userDetails.getUsername(), "");
    }

    /**
     * Registra un nuevo usuario y genera un token JWT
     */
    public JwtResponse registerUser(User user) {
        // En un caso real, aquí guardarías el usuario en la base de datos
        // Por ahora, simulamos la creación del usuario
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        
        // Crear UserDetails para el nuevo usuario
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(encodedPassword)
                .authorities(new ArrayList<>())
                .build();

        String token = jwtService.generateToken(userDetails);

        return new JwtResponse(token, user.getUsername(), user.getEmail());
    }
}
