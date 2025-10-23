package com.example.jwtapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.jwtapi.service.JwtService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/protected")
@CrossOrigin(origins = "*")
@Tag(name = "Endpoints Protegidos", description = "Endpoints que requieren autenticación JWT")
@SecurityRequirement(name = "Bearer Authentication")
public class ProtectedController {

    @Autowired
    private JwtService jwtService;

    /**
     * Endpoint protegido que requiere autenticación JWT
     * GET /api/protected/profile
     */
    @Operation(summary = "Obtener perfil de usuario", description = "Retorna información del perfil del usuario autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil obtenido exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error en la autenticación o token inválido")
    })
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(HttpServletRequest request) {
        try {
            String token = extractTokenFromRequest(request);
            String username = jwtService.extractUsername(token);
            
            return ResponseEntity.ok("Hola " + username + ", este es un endpoint protegido!");
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Error al acceder al perfil: " + e.getMessage());
        }
    }

    /**
     * Endpoint protegido para obtener información del usuario
     * GET /api/protected/user-info
     */
    @Operation(summary = "Obtener información del usuario", description = "Retorna información básica del usuario autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Información del usuario obtenida exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error en la autenticación o token inválido")
    })
    @GetMapping("/user-info")
    public ResponseEntity<?> getUserInfo(HttpServletRequest request) {
        try {
            String token = extractTokenFromRequest(request);
            String username = jwtService.extractUsername(token);
            
            return ResponseEntity.ok("Usuario autenticado: " + username);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Error al obtener información del usuario: " + e.getMessage());
        }
    }

    /**
     * Extrae el token JWT del header Authorization
     */
    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        throw new RuntimeException("Token no encontrado en el header Authorization");
    }
}
