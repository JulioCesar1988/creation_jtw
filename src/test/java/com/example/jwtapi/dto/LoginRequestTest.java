package com.example.jwtapi.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginRequestTest {

    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest();
    }

    @Test
    void defaultConstructor_CreatesEmptyRequest() {
        // Act
        LoginRequest newRequest = new LoginRequest();

        // Assert
        assertNotNull(newRequest);
        assertNull(newRequest.getUsername());
        assertNull(newRequest.getPassword());
    }

    @Test
    void parameterizedConstructor_CreatesRequestWithValues() {
        // Arrange
        String username = "testuser";
        String password = "password123";

        // Act
        LoginRequest newRequest = new LoginRequest(username, password);

        // Assert
        assertNotNull(newRequest);
        assertEquals(username, newRequest.getUsername());
        assertEquals(password, newRequest.getPassword());
    }

    @Test
    void setUsername_ValidValue_SetsUsername() {
        // Act
        loginRequest.setUsername("testuser");

        // Assert
        assertEquals("testuser", loginRequest.getUsername());
    }

    @Test
    void setUsername_NullValue_SetsNull() {
        // Act
        loginRequest.setUsername(null);

        // Assert
        assertNull(loginRequest.getUsername());
    }

    @Test
    void setUsername_EmptyValue_SetsEmpty() {
        // Act
        loginRequest.setUsername("");

        // Assert
        assertEquals("", loginRequest.getUsername());
    }

    @Test
    void getUsername_AfterSetting_ReturnsCorrectValue() {
        // Arrange
        String expectedUsername = "admin";
        loginRequest.setUsername(expectedUsername);

        // Act
        String result = loginRequest.getUsername();

        // Assert
        assertEquals(expectedUsername, result);
    }

    @Test
    void setPassword_ValidValue_SetsPassword() {
        // Act
        loginRequest.setPassword("password123");

        // Assert
        assertEquals("password123", loginRequest.getPassword());
    }

    @Test
    void setPassword_NullValue_SetsNull() {
        // Act
        loginRequest.setPassword(null);

        // Assert
        assertNull(loginRequest.getPassword());
    }

    @Test
    void setPassword_EmptyValue_SetsEmpty() {
        // Act
        loginRequest.setPassword("");

        // Assert
        assertEquals("", loginRequest.getPassword());
    }

    @Test
    void getPassword_AfterSetting_ReturnsCorrectValue() {
        // Arrange
        String expectedPassword = "securePassword123";
        loginRequest.setPassword(expectedPassword);

        // Act
        String result = loginRequest.getPassword();

        // Assert
        assertEquals(expectedPassword, result);
    }

    @Test
    void setAndGetAllFields_WorksCorrectly() {
        // Arrange
        String username = "testuser";
        String password = "password123";

        // Act
        loginRequest.setUsername(username);
        loginRequest.setPassword(password);

        // Assert
        assertEquals(username, loginRequest.getUsername());
        assertEquals(password, loginRequest.getPassword());
    }

    @Test
    void parameterizedConstructor_WithNullValues_HandlesCorrectly() {
        // Act
        LoginRequest newRequest = new LoginRequest(null, null);

        // Assert
        assertNotNull(newRequest);
        assertNull(newRequest.getUsername());
        assertNull(newRequest.getPassword());
    }

    @Test
    void parameterizedConstructor_WithEmptyValues_HandlesCorrectly() {
        // Act
        LoginRequest newRequest = new LoginRequest("", "");

        // Assert
        assertNotNull(newRequest);
        assertEquals("", newRequest.getUsername());
        assertEquals("", newRequest.getPassword());
    }

    @Test
    void setUsername_WithSpecialCharacters_HandlesCorrectly() {
        // Act
        loginRequest.setUsername("user@domain.com");

        // Assert
        assertEquals("user@domain.com", loginRequest.getUsername());
    }

    @Test
    void setPassword_WithSpecialCharacters_HandlesCorrectly() {
        // Act
        loginRequest.setPassword("P@ssw0rd!#$%");

        // Assert
        assertEquals("P@ssw0rd!#$%", loginRequest.getPassword());
    }

    @Test
    void setUsername_WithWhitespace_HandlesCorrectly() {
        // Act
        loginRequest.setUsername("  testuser  ");

        // Assert
        assertEquals("  testuser  ", loginRequest.getUsername());
    }

    @Test
    void setPassword_WithWhitespace_HandlesCorrectly() {
        // Act
        loginRequest.setPassword("  password  ");

        // Assert
        assertEquals("  password  ", loginRequest.getPassword());
    }

    @Test
    void setUsername_WithUnicode_HandlesCorrectly() {
        // Act
        loginRequest.setUsername("usuario_ñáéíóú");

        // Assert
        assertEquals("usuario_ñáéíóú", loginRequest.getUsername());
    }

    @Test
    void setPassword_WithUnicode_HandlesCorrectly() {
        // Act
        loginRequest.setPassword("contraseña_ñáéíóú");

        // Assert
        assertEquals("contraseña_ñáéíóú", loginRequest.getPassword());
    }
}


