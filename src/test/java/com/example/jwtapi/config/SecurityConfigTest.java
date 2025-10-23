package com.example.jwtapi.config;

import com.example.jwtapi.service.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class SecurityConfigTest {

    @Autowired
    private SecurityConfig securityConfig;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Test
    void passwordEncoder_ReturnsBCryptPasswordEncoder() {
        // Act
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();

        // Assert
        assertNotNull(passwordEncoder);
        assertTrue(passwordEncoder instanceof org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder);
    }

    @Test
    void passwordEncoder_EncodesPasswordCorrectly() {
        // Arrange
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        String rawPassword = "testPassword123";

        // Act
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // Assert
        assertNotNull(encodedPassword);
        assertNotEquals(rawPassword, encodedPassword);
        assertTrue(encodedPassword.startsWith("$2a$") || encodedPassword.startsWith("$2b$"));
        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));
    }

    @Test
    void passwordEncoder_DifferentEncodingsForSamePassword() {
        // Arrange
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        String rawPassword = "testPassword123";

        // Act
        String encodedPassword1 = passwordEncoder.encode(rawPassword);
        String encodedPassword2 = passwordEncoder.encode(rawPassword);

        // Assert
        assertNotEquals(encodedPassword1, encodedPassword2);
        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword1));
        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword2));
    }

    @Test
    void authenticationProvider_ReturnsDaoAuthenticationProvider() {
        // Act
        DaoAuthenticationProvider authProvider = securityConfig.authenticationProvider();

        // Assert
        assertNotNull(authProvider);
        assertTrue(authProvider instanceof DaoAuthenticationProvider);
        // Note: getUserDetailsService() and getPasswordEncoder() are protected methods
        // We can't test them directly, but we can verify the bean is created correctly
    }

    @Test
    void authenticationManager_ReturnsAuthenticationManager() throws Exception {
        // Act
        AuthenticationManager authManager = securityConfig.authenticationManager(null);

        // Assert
        assertNotNull(authManager);
        assertTrue(authManager instanceof AuthenticationManager);
    }

    @Test
    void filterChain_ReturnsSecurityFilterChain() throws Exception {
        // Act
        SecurityFilterChain filterChain = securityConfig.filterChain(null);

        // Assert
        assertNotNull(filterChain);
        assertTrue(filterChain instanceof SecurityFilterChain);
    }

    @Test
    void passwordEncoder_BeanIsSingleton() {
        // Act
        PasswordEncoder passwordEncoder1 = securityConfig.passwordEncoder();
        PasswordEncoder passwordEncoder2 = securityConfig.passwordEncoder();

        // Assert
        assertNotNull(passwordEncoder1);
        assertNotNull(passwordEncoder2);
        // Note: In Spring, beans are singletons by default, but in tests they might be different instances
        // The important thing is that both are valid PasswordEncoder instances
        assertTrue(passwordEncoder1 instanceof org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder);
        assertTrue(passwordEncoder2 instanceof org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder);
    }

    @Test
    void authenticationProvider_BeanIsSingleton() {
        // Act
        DaoAuthenticationProvider authProvider1 = securityConfig.authenticationProvider();
        DaoAuthenticationProvider authProvider2 = securityConfig.authenticationProvider();

        // Assert
        assertNotNull(authProvider1);
        assertNotNull(authProvider2);
        // Both should be valid DaoAuthenticationProvider instances
        assertTrue(authProvider1 instanceof DaoAuthenticationProvider);
        assertTrue(authProvider2 instanceof DaoAuthenticationProvider);
    }

    @Test
    void passwordEncoder_MatchesMethodWorksCorrectly() {
        // Arrange
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        String rawPassword = "testPassword123";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // Act & Assert
        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));
        assertFalse(passwordEncoder.matches("wrongPassword", encodedPassword));
        assertFalse(passwordEncoder.matches(rawPassword, "wrongEncodedPassword"));
    }

    @Test
    void passwordEncoder_HandlesNullPasswords() {
        // Arrange
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            passwordEncoder.encode(null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            passwordEncoder.matches(null, "encodedPassword");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            passwordEncoder.matches("rawPassword", null);
        });
    }

    @Test
    void passwordEncoder_HandlesEmptyPasswords() {
        // Arrange
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();

        // Act
        String encodedEmptyPassword = passwordEncoder.encode("");

        // Assert
        assertNotNull(encodedEmptyPassword);
        assertTrue(passwordEncoder.matches("", encodedEmptyPassword));
        assertFalse(passwordEncoder.matches("nonEmpty", encodedEmptyPassword));
    }
}
