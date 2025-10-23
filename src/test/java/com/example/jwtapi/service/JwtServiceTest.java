package com.example.jwtapi.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    private UserDetails userDetails;
    private String secretKey;
    private long jwtExpiration;

    @BeforeEach
    void setUp() {
        // Configurar valores de prueba
        secretKey = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
        jwtExpiration = TimeUnit.HOURS.toMillis(24); // 24 horas

        // Inyectar valores usando ReflectionTestUtils
        ReflectionTestUtils.setField(jwtService, "secretKey", secretKey);
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", jwtExpiration);

        userDetails = User.builder()
                .username("testuser")
                .password("password")
                .authorities(new ArrayList<>())
                .build();
    }

    @Test
    void generateToken_ValidUserDetails_ReturnsToken() {
        // Act
        String token = jwtService.generateToken(userDetails);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.split("\\.").length == 3); // JWT tiene 3 partes separadas por puntos
    }

    @Test
    void generateToken_WithExtraClaims_ReturnsToken() {
        // Arrange
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", "ADMIN");
        extraClaims.put("userId", 123L);

        // Act
        String token = jwtService.generateToken(extraClaims, userDetails);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.split("\\.").length == 3);
    }

    @Test
    void extractUsername_ValidToken_ReturnsUsername() {
        // Arrange
        String token = jwtService.generateToken(userDetails);

        // Act
        String username = jwtService.extractUsername(token);

        // Assert
        assertEquals("testuser", username);
    }

    @Test
    void extractUsername_InvalidToken_ThrowsException() {
        // Arrange
        String invalidToken = "invalid.token.here";

        // Act & Assert
        assertThrows(Exception.class, () -> {
            jwtService.extractUsername(invalidToken);
        });
    }

    @Test
    void extractClaim_ValidToken_ReturnsClaim() {
        // Arrange
        String token = jwtService.generateToken(userDetails);

        // Act
        String subject = jwtService.extractClaim(token, Claims::getSubject);
        Date issuedAt = jwtService.extractClaim(token, Claims::getIssuedAt);
        Date expiration = jwtService.extractClaim(token, Claims::getExpiration);

        // Assert
        assertEquals("testuser", subject);
        assertNotNull(issuedAt);
        assertNotNull(expiration);
        assertTrue(expiration.after(issuedAt));
    }

    @Test
    void isTokenValid_ValidTokenAndUser_ReturnsTrue() {
        // Arrange
        String token = jwtService.generateToken(userDetails);

        // Act
        boolean isValid = jwtService.isTokenValid(token, userDetails);

        // Assert
        assertTrue(isValid);
    }

    @Test
    void isTokenValid_InvalidUsername_ReturnsFalse() {
        // Arrange
        String token = jwtService.generateToken(userDetails);
        UserDetails differentUser = User.builder()
                .username("differentuser")
                .password("password")
                .authorities(new ArrayList<>())
                .build();

        // Act
        boolean isValid = jwtService.isTokenValid(token, differentUser);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void isTokenValid_ExpiredToken_ReturnsFalse() {
        // Arrange
        // Crear un token con expiración muy corta
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", 1L); // 1ms
        String token = jwtService.generateToken(userDetails);
        
        // Esperar a que expire
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Act
        boolean isValid = jwtService.isTokenValid(token, userDetails);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void isTokenValid_InvalidToken_ThrowsException() {
        // Arrange
        String invalidToken = "invalid.token.here";

        // Act & Assert
        assertThrows(Exception.class, () -> {
            jwtService.isTokenValid(invalidToken, userDetails);
        });
    }

    @Test
    void generateToken_DifferentUsers_DifferentTokens() {
        // Arrange
        UserDetails user1 = User.builder()
                .username("user1")
                .password("password")
                .authorities(new ArrayList<>())
                .build();

        UserDetails user2 = User.builder()
                .username("user2")
                .password("password")
                .authorities(new ArrayList<>())
                .build();

        // Act
        String token1 = jwtService.generateToken(user1);
        String token2 = jwtService.generateToken(user2);

        // Assert
        assertNotEquals(token1, token2);
        assertEquals("user1", jwtService.extractUsername(token1));
        assertEquals("user2", jwtService.extractUsername(token2));
    }

    @Test
    void generateToken_SameUserMultipleTimes_DifferentTokens() {
        // Act
        String token1 = jwtService.generateToken(userDetails);
        try {
            Thread.sleep(1); // Pequeña pausa para asegurar diferentes timestamps
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        String token2 = jwtService.generateToken(userDetails);

        // Assert
        assertNotEquals(token1, token2);
        assertEquals("testuser", jwtService.extractUsername(token1));
        assertEquals("testuser", jwtService.extractUsername(token2));
    }

    @Test
    void extractClaim_WithCustomFunction_ReturnsCorrectValue() {
        // Arrange
        String token = jwtService.generateToken(userDetails);

        // Act
        String subject = jwtService.extractClaim(token, claims -> claims.getSubject());
        Date issuedAt = jwtService.extractClaim(token, claims -> claims.getIssuedAt());

        // Assert
        assertEquals("testuser", subject);
        assertNotNull(issuedAt);
    }
}

