package com.example.jwtapi.controller;

import com.example.jwtapi.dto.JwtResponse;
import com.example.jwtapi.dto.LoginRequest;
import com.example.jwtapi.model.User;
import com.example.jwtapi.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@Tag(name = "Autenticación", description = "Endpoints para autenticación y registro de usuarios")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * Endpoint para autenticar un usuario existente
     * POST /api/auth/login
     */
    @Operation(summary = "Autenticar usuario", description = "Autentica un usuario existente y retorna un token JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticación exitosa",
                    content = @Content(schema = @Schema(implementation = JwtResponse.class))),
            @ApiResponse(responseCode = "400", description = "Credenciales inválidas")
    })
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            JwtResponse response = authService.authenticateUser(loginRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Error en la autenticación: " + e.getMessage());
        }
    }

    /**
     * Endpoint para registrar un nuevo usuario
     * POST /api/auth/register
     */
    @Operation(summary = "Registrar usuario", description = "Registra un nuevo usuario y retorna un token JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registro exitoso",
                    content = @Content(schema = @Schema(implementation = JwtResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos de usuario inválidos")
    })
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user) {
        try {
            JwtResponse response = authService.registerUser(user);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Error en el registro: " + e.getMessage());
        }
    }

    /**
     * Endpoint de prueba para verificar que el servidor está funcionando
     * GET /api/auth/test
     */
    @Operation(summary = "Probar API", description = "Endpoint de prueba para verificar que la API está funcionando")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "API funcionando correctamente")
    })
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("API JWT funcionando correctamente");
    }
}
