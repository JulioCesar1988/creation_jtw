package com.example.jwtapi.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtResponseTest {

    private JwtResponse jwtResponse;

    @BeforeEach
    void setUp() {
        jwtResponse = new JwtResponse();
    }

    @Test
    void defaultConstructor_CreatesEmptyResponse() {
        // Act
        JwtResponse newResponse = new JwtResponse();

        // Assert
        assertNotNull(newResponse);
        assertNull(newResponse.getToken());
        assertEquals("Bearer", newResponse.getType());
        assertNull(newResponse.getUsername());
        assertNull(newResponse.getEmail());
    }

    @Test
    void parameterizedConstructor_CreatesResponseWithValues() {
        // Arrange
        String token = "jwt-token";
        String username = "testuser";
        String email = "test@example.com";

        // Act
        JwtResponse newResponse = new JwtResponse(token, username, email);

        // Assert
        assertNotNull(newResponse);
        assertEquals(token, newResponse.getToken());
        assertEquals("Bearer", newResponse.getType());
        assertEquals(username, newResponse.getUsername());
        assertEquals(email, newResponse.getEmail());
    }

    @Test
    void setToken_ValidValue_SetsToken() {
        // Act
        jwtResponse.setToken("jwt-token");

        // Assert
        assertEquals("jwt-token", jwtResponse.getToken());
    }

    @Test
    void setToken_NullValue_SetsNull() {
        // Act
        jwtResponse.setToken(null);

        // Assert
        assertNull(jwtResponse.getToken());
    }

    @Test
    void setToken_EmptyValue_SetsEmpty() {
        // Act
        jwtResponse.setToken("");

        // Assert
        assertEquals("", jwtResponse.getToken());
    }

    @Test
    void getToken_AfterSetting_ReturnsCorrectValue() {
        // Arrange
        String expectedToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...";
        jwtResponse.setToken(expectedToken);

        // Act
        String result = jwtResponse.getToken();

        // Assert
        assertEquals(expectedToken, result);
    }

    @Test
    void setType_ValidValue_SetsType() {
        // Act
        jwtResponse.setType("JWT");

        // Assert
        assertEquals("JWT", jwtResponse.getType());
    }

    @Test
    void setType_NullValue_SetsNull() {
        // Act
        jwtResponse.setType(null);

        // Assert
        assertNull(jwtResponse.getType());
    }

    @Test
    void setType_EmptyValue_SetsEmpty() {
        // Act
        jwtResponse.setType("");

        // Assert
        assertEquals("", jwtResponse.getType());
    }

    @Test
    void getType_AfterSetting_ReturnsCorrectValue() {
        // Arrange
        String expectedType = "Custom";
        jwtResponse.setType(expectedType);

        // Act
        String result = jwtResponse.getType();

        // Assert
        assertEquals(expectedType, result);
    }

    @Test
    void setUsername_ValidValue_SetsUsername() {
        // Act
        jwtResponse.setUsername("testuser");

        // Assert
        assertEquals("testuser", jwtResponse.getUsername());
    }

    @Test
    void setUsername_NullValue_SetsNull() {
        // Act
        jwtResponse.setUsername(null);

        // Assert
        assertNull(jwtResponse.getUsername());
    }

    @Test
    void setUsername_EmptyValue_SetsEmpty() {
        // Act
        jwtResponse.setUsername("");

        // Assert
        assertEquals("", jwtResponse.getUsername());
    }

    @Test
    void getUsername_AfterSetting_ReturnsCorrectValue() {
        // Arrange
        String expectedUsername = "admin";
        jwtResponse.setUsername(expectedUsername);

        // Act
        String result = jwtResponse.getUsername();

        // Assert
        assertEquals(expectedUsername, result);
    }

    @Test
    void setEmail_ValidValue_SetsEmail() {
        // Act
        jwtResponse.setEmail("test@example.com");

        // Assert
        assertEquals("test@example.com", jwtResponse.getEmail());
    }

    @Test
    void setEmail_NullValue_SetsNull() {
        // Act
        jwtResponse.setEmail(null);

        // Assert
        assertNull(jwtResponse.getEmail());
    }

    @Test
    void setEmail_EmptyValue_SetsEmpty() {
        // Act
        jwtResponse.setEmail("");

        // Assert
        assertEquals("", jwtResponse.getEmail());
    }

    @Test
    void getEmail_AfterSetting_ReturnsCorrectValue() {
        // Arrange
        String expectedEmail = "admin@example.com";
        jwtResponse.setEmail(expectedEmail);

        // Act
        String result = jwtResponse.getEmail();

        // Assert
        assertEquals(expectedEmail, result);
    }

    @Test
    void setAndGetAllFields_WorksCorrectly() {
        // Arrange
        String token = "jwt-token";
        String type = "Bearer";
        String username = "testuser";
        String email = "test@example.com";

        // Act
        jwtResponse.setToken(token);
        jwtResponse.setType(type);
        jwtResponse.setUsername(username);
        jwtResponse.setEmail(email);

        // Assert
        assertEquals(token, jwtResponse.getToken());
        assertEquals(type, jwtResponse.getType());
        assertEquals(username, jwtResponse.getUsername());
        assertEquals(email, jwtResponse.getEmail());
    }

    @Test
    void parameterizedConstructor_WithNullValues_HandlesCorrectly() {
        // Act
        JwtResponse newResponse = new JwtResponse(null, null, null);

        // Assert
        assertNotNull(newResponse);
        assertNull(newResponse.getToken());
        assertEquals("Bearer", newResponse.getType());
        assertNull(newResponse.getUsername());
        assertNull(newResponse.getEmail());
    }

    @Test
    void parameterizedConstructor_WithEmptyValues_HandlesCorrectly() {
        // Act
        JwtResponse newResponse = new JwtResponse("", "", "");

        // Assert
        assertNotNull(newResponse);
        assertEquals("", newResponse.getToken());
        assertEquals("Bearer", newResponse.getType());
        assertEquals("", newResponse.getUsername());
        assertEquals("", newResponse.getEmail());
    }

    @Test
    void defaultType_IsBearer() {
        // Act
        JwtResponse newResponse = new JwtResponse();

        // Assert
        assertEquals("Bearer", newResponse.getType());
    }

    @Test
    void parameterizedConstructor_PreservesDefaultType() {
        // Act
        JwtResponse newResponse = new JwtResponse("token", "user", "email");

        // Assert
        assertEquals("Bearer", newResponse.getType());
    }
}

