package com.example.jwtapi.controller;

import com.example.jwtapi.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/protected")
@CrossOrigin(origins = "*")
public class ProtectedController {

    @Autowired
    private JwtService jwtService;

    /**
     * Endpoint protegido que requiere autenticación JWT
     * GET /api/protected/profile
     */
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
