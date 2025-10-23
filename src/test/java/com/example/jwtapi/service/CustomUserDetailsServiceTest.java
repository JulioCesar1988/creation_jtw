package com.example.jwtapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    void setUp() {
        // No setup needed for this test
    }

    @Test
    void loadUserByUsername_AdminUser_ReturnsUserDetails() {
        // Act
        UserDetails result = customUserDetailsService.loadUserByUsername("admin");

        // Assert
        assertNotNull(result);
        assertEquals("admin", result.getUsername());
        assertNotNull(result.getPassword());
        assertFalse(result.getPassword().isEmpty());
        assertTrue(result.getAuthorities().isEmpty());
        assertTrue(result.isAccountNonExpired());
        assertTrue(result.isAccountNonLocked());
        assertTrue(result.isCredentialsNonExpired());
        assertTrue(result.isEnabled());
    }

    @Test
    void loadUserByUsername_NonAdminUser_ThrowsUsernameNotFoundException() {
        // Act & Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername("nonadmin");
        });

        assertEquals("Usuario no encontrado: nonadmin", exception.getMessage());
    }

    @Test
    void loadUserByUsername_EmptyUsername_ThrowsUsernameNotFoundException() {
        // Act & Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername("");
        });

        assertEquals("Usuario no encontrado: ", exception.getMessage());
    }

    @Test
    void loadUserByUsername_NullUsername_ThrowsUsernameNotFoundException() {
        // Act & Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername(null);
        });

        assertEquals("Usuario no encontrado: null", exception.getMessage());
    }

    @Test
    void loadUserByUsername_AdminUserCaseSensitive_ThrowsUsernameNotFoundException() {
        // Act & Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername("Admin");
        });

        assertEquals("Usuario no encontrado: Admin", exception.getMessage());
    }

    @Test
    void loadUserByUsername_AdminUserWithSpaces_ThrowsUsernameNotFoundException() {
        // Act & Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername(" admin ");
        });

        assertEquals("Usuario no encontrado:  admin ", exception.getMessage());
    }

    @Test
    void loadUserByUsername_AdminUserMultipleCalls_ReturnsConsistentUserDetails() {
        // Act
        UserDetails result1 = customUserDetailsService.loadUserByUsername("admin");
        UserDetails result2 = customUserDetailsService.loadUserByUsername("admin");

        // Assert
        assertNotNull(result1);
        assertNotNull(result2);
        assertEquals(result1.getUsername(), result2.getUsername());
        // Note: Passwords will be different due to BCrypt encoding, but both should be valid
        assertNotNull(result1.getPassword());
        assertNotNull(result2.getPassword());
        assertNotEquals(result1.getPassword(), result2.getPassword()); // BCrypt generates different hashes
    }

    @Test
    void loadUserByUsername_AdminUser_ReturnsUserWithCorrectPassword() {
        // Act
        UserDetails result = customUserDetailsService.loadUserByUsername("admin");

        // Assert
        assertNotNull(result);
        assertEquals("admin", result.getUsername());
        
        // Verify that the password is BCrypt encoded (starts with $2a$)
        String password = result.getPassword();
        assertTrue(password.startsWith("$2a$") || password.startsWith("$2b$"));
        assertTrue(password.length() > 50); // BCrypt hashes are typically 60 characters
    }
}

