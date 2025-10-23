package com.example.jwtapi.controller;

import com.example.jwtapi.dto.JwtResponse;
import com.example.jwtapi.dto.LoginRequest;
import com.example.jwtapi.model.User;
import com.example.jwtapi.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    private LoginRequest loginRequest;
    private User user;
    private JwtResponse jwtResponse;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest("testuser", "password123");
        user = new User("testuser", "test@example.com", "password123");
        jwtResponse = new JwtResponse("jwt-token", "testuser", "test@example.com");
    }

    @Test
    void authenticateUser_ValidCredentials_ReturnsJwtResponse() throws Exception {
        // Arrange
        when(authService.authenticateUser(any(LoginRequest.class))).thenReturn(jwtResponse);

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.type").value("Bearer"));
    }

    @Test
    void authenticateUser_InvalidCredentials_ReturnsBadRequest() throws Exception {
        // Arrange
        when(authService.authenticateUser(any(LoginRequest.class)))
                .thenThrow(new RuntimeException("Invalid credentials"));

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error en la autenticación: Invalid credentials"));
    }

    @Test
    void authenticateUser_EmptyUsername_ReturnsBadRequest() throws Exception {
        // Arrange
        LoginRequest invalidRequest = new LoginRequest("", "password123");

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void authenticateUser_EmptyPassword_ReturnsBadRequest() throws Exception {
        // Arrange
        LoginRequest invalidRequest = new LoginRequest("testuser", "");

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void authenticateUser_MissingFields_ReturnsBadRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registerUser_ValidUser_ReturnsJwtResponse() throws Exception {
        // Arrange
        when(authService.registerUser(any(User.class))).thenReturn(jwtResponse);

        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.type").value("Bearer"));
    }

    @Test
    void registerUser_InvalidUser_ReturnsBadRequest() throws Exception {
        // Arrange
        when(authService.registerUser(any(User.class)))
                .thenThrow(new RuntimeException("Invalid user data"));

        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error en el registro: Invalid user data"));
    }

    @Test
    void registerUser_EmptyUsername_ReturnsBadRequest() throws Exception {
        // Arrange
        User invalidUser = new User("", "test@example.com", "password123");

        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registerUser_InvalidEmail_ReturnsBadRequest() throws Exception {
        // Arrange
        User invalidUser = new User("testuser", "invalid-email", "password123");

        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registerUser_ShortPassword_ReturnsBadRequest() throws Exception {
        // Arrange
        User invalidUser = new User("testuser", "test@example.com", "123");

        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registerUser_MissingFields_ReturnsBadRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void test_ReturnsSuccessMessage() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/auth/test"))
                .andExpect(status().isOk())
                .andExpect(content().string("API JWT funcionando correctamente"));
    }

    @Test
    void authenticateUser_InvalidJson_ReturnsBadRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("invalid json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registerUser_InvalidJson_ReturnsBadRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("invalid json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void authenticateUser_NullRequest_ReturnsBadRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("null"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registerUser_NullRequest_ReturnsBadRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("null"))
                .andExpect(status().isBadRequest());
    }
}

