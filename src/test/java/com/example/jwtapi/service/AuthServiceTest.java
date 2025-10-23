package com.example.jwtapi.service;

import com.example.jwtapi.dto.JwtResponse;
import com.example.jwtapi.dto.LoginRequest;
import com.example.jwtapi.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private AuthService authService;

    private LoginRequest loginRequest;
    private User user;
    private UserDetails userDetails;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest("testuser", "password123");
        user = new User("testuser", "test@example.com", "password123");
        
        userDetails = org.springframework.security.core.userdetails.User.builder()
                .username("testuser")
                .password("encodedPassword")
                .authorities(new ArrayList<>())
                .build();

        authentication = mock(Authentication.class);
    }

    @Test
    void authenticateUser_ValidCredentials_ReturnsJwtResponse() {
        // Arrange
        String expectedToken = "jwt-token";
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtService.generateToken(userDetails)).thenReturn(expectedToken);

        // Act
        JwtResponse result = authService.authenticateUser(loginRequest);

        // Assert
        assertNotNull(result);
        assertEquals(expectedToken, result.getToken());
        assertEquals("testuser", result.getUsername());
        assertEquals("Bearer", result.getType());
        assertNull(result.getEmail());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService).generateToken(userDetails);
    }

    @Test
    void authenticateUser_InvalidCredentials_ThrowsException() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Invalid credentials"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            authService.authenticateUser(loginRequest);
        });

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    void registerUser_ValidUser_ReturnsJwtResponse() {
        // Arrange
        String expectedToken = "jwt-token";
        String encodedPassword = "encodedPassword";
        when(passwordEncoder.encode(anyString())).thenReturn(encodedPassword);
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn(expectedToken);

        // Act
        JwtResponse result = authService.registerUser(user);

        // Assert
        assertNotNull(result);
        assertEquals(expectedToken, result.getToken());
        assertEquals("testuser", result.getUsername());
        assertEquals("test@example.com", result.getEmail());
        assertEquals("Bearer", result.getType());

        verify(passwordEncoder).encode("password123");
        verify(jwtService).generateToken(any(UserDetails.class));
    }

    @Test
    void registerUser_NullUser_ThrowsException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            authService.registerUser(null);
        });
    }

    @Test
    void registerUser_UserWithNullPassword_ThrowsException() {
        // Arrange
        User userWithNullPassword = new User("testuser", "test@example.com", null);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            authService.registerUser(userWithNullPassword);
        });
    }

    @Test
    void authenticateUser_LoginRequestWithNullUsername_ThrowsException() {
        // Arrange
        LoginRequest invalidRequest = new LoginRequest(null, "password");

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            authService.authenticateUser(invalidRequest);
        });
    }

    @Test
    void authenticateUser_LoginRequestWithNullPassword_ThrowsException() {
        // Arrange
        LoginRequest invalidRequest = new LoginRequest("username", null);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            authService.authenticateUser(invalidRequest);
        });
    }
}

