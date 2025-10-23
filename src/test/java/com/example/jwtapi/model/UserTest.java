package com.example.jwtapi.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    void defaultConstructor_CreatesEmptyUser() {
        // Act
        User newUser = new User();

        // Assert
        assertNotNull(newUser);
        assertNull(newUser.getUsername());
        assertNull(newUser.getEmail());
        assertNull(newUser.getPassword());
    }

    @Test
    void parameterizedConstructor_CreatesUserWithValues() {
        // Act
        User newUser = new User("testuser", "test@example.com", "password123");

        // Assert
        assertNotNull(newUser);
        assertEquals("testuser", newUser.getUsername());
        assertEquals("test@example.com", newUser.getEmail());
        assertEquals("password123", newUser.getPassword());
    }

    @Test
    void setUsername_ValidValue_SetsUsername() {
        // Act
        user.setUsername("testuser");

        // Assert
        assertEquals("testuser", user.getUsername());
    }

    @Test
    void setUsername_NullValue_SetsNull() {
        // Act
        user.setUsername(null);

        // Assert
        assertNull(user.getUsername());
    }

    @Test
    void setUsername_EmptyValue_SetsEmpty() {
        // Act
        user.setUsername("");

        // Assert
        assertEquals("", user.getUsername());
    }

    @Test
    void getUsername_AfterSetting_ReturnsCorrectValue() {
        // Arrange
        String expectedUsername = "testuser";
        user.setUsername(expectedUsername);

        // Act
        String result = user.getUsername();

        // Assert
        assertEquals(expectedUsername, result);
    }

    @Test
    void setEmail_ValidValue_SetsEmail() {
        // Act
        user.setEmail("test@example.com");

        // Assert
        assertEquals("test@example.com", user.getEmail());
    }

    @Test
    void setEmail_NullValue_SetsNull() {
        // Act
        user.setEmail(null);

        // Assert
        assertNull(user.getEmail());
    }

    @Test
    void setEmail_EmptyValue_SetsEmpty() {
        // Act
        user.setEmail("");

        // Assert
        assertEquals("", user.getEmail());
    }

    @Test
    void getEmail_AfterSetting_ReturnsCorrectValue() {
        // Arrange
        String expectedEmail = "test@example.com";
        user.setEmail(expectedEmail);

        // Act
        String result = user.getEmail();

        // Assert
        assertEquals(expectedEmail, result);
    }

    @Test
    void setPassword_ValidValue_SetsPassword() {
        // Act
        user.setPassword("password123");

        // Assert
        assertEquals("password123", user.getPassword());
    }

    @Test
    void setPassword_NullValue_SetsNull() {
        // Act
        user.setPassword(null);

        // Assert
        assertNull(user.getPassword());
    }

    @Test
    void setPassword_EmptyValue_SetsEmpty() {
        // Act
        user.setPassword("");

        // Assert
        assertEquals("", user.getPassword());
    }

    @Test
    void getPassword_AfterSetting_ReturnsCorrectValue() {
        // Arrange
        String expectedPassword = "password123";
        user.setPassword(expectedPassword);

        // Act
        String result = user.getPassword();

        // Assert
        assertEquals(expectedPassword, result);
    }

    @Test
    void toString_WithAllFields_ReturnsCorrectString() {
        // Arrange
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password123");

        // Act
        String result = user.toString();

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("testuser"));
        assertTrue(result.contains("test@example.com"));
        assertFalse(result.contains("password123")); // Password should not be in toString
    }

    @Test
    void toString_WithNullFields_ReturnsCorrectString() {
        // Act
        String result = user.toString();

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("null"));
    }

    @Test
    void toString_WithEmptyFields_ReturnsCorrectString() {
        // Arrange
        user.setUsername("");
        user.setEmail("");

        // Act
        String result = user.toString();

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("''"));
    }

    @Test
    void setAndGetAllFields_WorksCorrectly() {
        // Arrange
        String username = "testuser";
        String email = "test@example.com";
        String password = "password123";

        // Act
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);

        // Assert
        assertEquals(username, user.getUsername());
        assertEquals(email, user.getEmail());
        assertEquals(password, user.getPassword());
    }

    @Test
    void parameterizedConstructor_WithNullValues_HandlesCorrectly() {
        // Act
        User newUser = new User(null, null, null);

        // Assert
        assertNotNull(newUser);
        assertNull(newUser.getUsername());
        assertNull(newUser.getEmail());
        assertNull(newUser.getPassword());
    }

    @Test
    void parameterizedConstructor_WithEmptyValues_HandlesCorrectly() {
        // Act
        User newUser = new User("", "", "");

        // Assert
        assertNotNull(newUser);
        assertEquals("", newUser.getUsername());
        assertEquals("", newUser.getEmail());
        assertEquals("", newUser.getPassword());
    }
}

