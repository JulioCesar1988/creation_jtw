package com.example.jwtapi.controller;

import com.example.jwtapi.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProtectedController.class)
class ProtectedControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        // Setup common mocks
    }

    @Test
    void getProfile_ValidToken_ReturnsProfileMessage() throws Exception {
        // Arrange
        when(jwtService.extractUsername(anyString())).thenReturn("testuser");

        // Act & Assert
        mockMvc.perform(get("/api/protected/profile")
                .header("Authorization", "Bearer valid-jwt-token"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hola testuser, este es un endpoint protegido!"));
    }

    @Test
    void getProfile_InvalidToken_ReturnsBadRequest() throws Exception {
        // Arrange
        when(jwtService.extractUsername(anyString()))
                .thenThrow(new RuntimeException("Invalid token"));

        // Act & Assert
        mockMvc.perform(get("/api/protected/profile")
                .header("Authorization", "Bearer invalid-jwt-token"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error al acceder al perfil: Invalid token"));
    }

    @Test
    void getProfile_MissingAuthorizationHeader_ReturnsBadRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/protected/profile"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error al acceder al perfil: Token no encontrado en el header Authorization"));
    }

    @Test
    void getProfile_InvalidAuthorizationFormat_ReturnsBadRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/protected/profile")
                .header("Authorization", "InvalidFormat token"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error al acceder al perfil: Token no encontrado en el header Authorization"));
    }

    @Test
    void getProfile_EmptyAuthorizationHeader_ReturnsBadRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/protected/profile")
                .header("Authorization", ""))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error al acceder al perfil: Token no encontrado en el header Authorization"));
    }

    @Test
    void getUserInfo_ValidToken_ReturnsUserInfo() throws Exception {
        // Arrange
        when(jwtService.extractUsername(anyString())).thenReturn("testuser");

        // Act & Assert
        mockMvc.perform(get("/api/protected/user-info")
                .header("Authorization", "Bearer valid-jwt-token"))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuario autenticado: testuser"));
    }

    @Test
    void getUserInfo_InvalidToken_ReturnsBadRequest() throws Exception {
        // Arrange
        when(jwtService.extractUsername(anyString()))
                .thenThrow(new RuntimeException("Invalid token"));

        // Act & Assert
        mockMvc.perform(get("/api/protected/user-info")
                .header("Authorization", "Bearer invalid-jwt-token"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error al obtener información del usuario: Invalid token"));
    }

    @Test
    void getUserInfo_MissingAuthorizationHeader_ReturnsBadRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/protected/user-info"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error al obtener información del usuario: Token no encontrado en el header Authorization"));
    }

    @Test
    void getUserInfo_InvalidAuthorizationFormat_ReturnsBadRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/protected/user-info")
                .header("Authorization", "InvalidFormat token"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error al obtener información del usuario: Token no encontrado en el header Authorization"));
    }

    @Test
    void getProfile_DifferentUsername_ReturnsCorrectMessage() throws Exception {
        // Arrange
        when(jwtService.extractUsername(anyString())).thenReturn("admin");

        // Act & Assert
        mockMvc.perform(get("/api/protected/profile")
                .header("Authorization", "Bearer valid-jwt-token"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hola admin, este es un endpoint protegido!"));
    }

    @Test
    void getUserInfo_DifferentUsername_ReturnsCorrectMessage() throws Exception {
        // Arrange
        when(jwtService.extractUsername(anyString())).thenReturn("admin");

        // Act & Assert
        mockMvc.perform(get("/api/protected/user-info")
                .header("Authorization", "Bearer valid-jwt-token"))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuario autenticado: admin"));
    }

    @Test
    void getProfile_ExpiredToken_ReturnsBadRequest() throws Exception {
        // Arrange
        when(jwtService.extractUsername(anyString()))
                .thenThrow(new RuntimeException("Token expired"));

        // Act & Assert
        mockMvc.perform(get("/api/protected/profile")
                .header("Authorization", "Bearer expired-jwt-token"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error al acceder al perfil: Token expired"));
    }

    @Test
    void getUserInfo_ExpiredToken_ReturnsBadRequest() throws Exception {
        // Arrange
        when(jwtService.extractUsername(anyString()))
                .thenThrow(new RuntimeException("Token expired"));

        // Act & Assert
        mockMvc.perform(get("/api/protected/user-info")
                .header("Authorization", "Bearer expired-jwt-token"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error al obtener información del usuario: Token expired"));
    }

    @Test
    void getProfile_AuthorizationHeaderWithSpaces_ReturnsBadRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/protected/profile")
                .header("Authorization", "Bearer "))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error al acceder al perfil: Token no encontrado en el header Authorization"));
    }

    @Test
    void getUserInfo_AuthorizationHeaderWithSpaces_ReturnsBadRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/protected/user-info")
                .header("Authorization", "Bearer "))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error al obtener información del usuario: Token no encontrado en el header Authorization"));
    }
}

