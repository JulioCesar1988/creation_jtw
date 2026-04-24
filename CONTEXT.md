# CONTEXT.md — Guía de arquitectura y estilo para IAs y colaboradores

Este archivo define las reglas técnicas y de estilo que deben respetarse al escribir o modificar código en este proyecto. Cualquier IA o desarrollador que trabaje aquí debe leerlo antes de generar código.

---

## 1. Stack Tecnológico

| Componente | Versión / Detalle |
|---|---|
| Lenguaje | Java 17 (LTS) |
| Framework | Spring Boot 3.2.0 |
| Seguridad | Spring Security 6.x (Jakarta EE) |
| Autenticación | JWT — JJWT 0.12.3 |
| Persistencia | Spring Data JPA + Hibernate |
| Base de datos | H2 (desarrollo) — reemplazable por PostgreSQL en producción |
| Hashing | BCrypt via `BCryptPasswordEncoder` |
| Build | Maven 3.x, empaquetado WAR |
| Documentación API | SpringDoc OpenAPI 2.x (Swagger UI) |
| Validación | `jakarta.validation` (Bean Validation 3.0) |
| Contenedores | Docker + Docker Compose |

---

## 2. Reglas de Estilo y Código

### 2.1 Inyección de dependencias
**Siempre usar inyección por constructor.** Está prohibido usar `@Autowired` en campos.

```java
// CORRECTO
@Service
public class AuthService {

    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }
}

// INCORRECTO — no hacer esto
@Autowired
private JwtService jwtService;
```

### 2.2 Records de Java 17
Usar `record` para DTOs, respuestas y objetos de valor inmutables. No crear clases con getters/setters manuales para estos casos.

```java
// CORRECTO
public record LoginRequest(
    @NotBlank String username,
    @NotBlank String password
) {}

public record JwtResponse(String token, String username, String email) {}

// INCORRECTO — no usar clases con boilerplate manual para DTOs
public class LoginRequest {
    private String username;
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}
```

### 2.3 Validaciones
Usar anotaciones de `jakarta.validation.constraints` en todos los DTOs y entidades expuestos al exterior. Activar con `@Valid` en los parámetros de los controllers.

```java
public record LoginRequest(
    @NotBlank(message = "El username es obligatorio")
    @Size(min = 3, max = 50)
    String username,

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6)
    String password
) {}
```

### 2.4 Manejo de errores
No capturar excepciones genéricas (`Exception`) en los controllers. Usar un `@RestControllerAdvice` centralizado para mapear excepciones a respuestas HTTP.

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("Credenciales inválidas"));
    }
}
```

### 2.5 Comentarios
No escribir comentarios que repitan lo que el nombre del método ya dice. Solo documentar decisiones no obvias, restricciones de negocio o workarounds.

---

## 3. Seguridad

### 3.1 Configuración de Spring Security
**No usar `WebSecurityConfigurerAdapter`** — está deprecado desde Spring Security 5.7 y eliminado en Spring Security 6. Toda la configuración debe hacerse mediante beans explícitos con la API lambda.

```java
// CORRECTO — Spring Security 6 con Jakarta EE
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

// INCORRECTO — no hacer esto nunca
public class SecurityConfig extends WebSecurityConfigurerAdapter { ... }
```

### 3.2 Filtro JWT
Todo request autenticado debe pasar por un `JwtAuthenticationFilter` que valide el token y cargue el `SecurityContext`. No extraer ni validar tokens manualmente dentro de los controllers.

```java
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        String username = jwtService.extractUsername(token);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (jwtService.isTokenValid(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
```

### 3.3 Secretos
El `jwt.secret` y credenciales de base de datos **nunca** deben estar hardcodeados en archivos de propiedades versionados. En producción, inyectar mediante variables de entorno o un gestor de secretos.

```properties
# Correcto en desarrollo local (archivo .env o variable de entorno)
JWT_SECRET=<valor-generado-externamente>

# Incorrecto — no commitear valores reales
jwt.secret=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
```

### 3.4 Endpoints de productos
Los endpoints de escritura (`POST`, `PUT`, `DELETE`, `PATCH`) sobre `/api/products/**` deben requerir autenticación. Solo `GET` puede ser público si el negocio lo requiere.

---

## 4. Arquitectura

### 4.1 Estilo general
El proyecto sigue una **arquitectura orientada a servicios (SOA / Layered)** con separación clara de responsabilidades. En módulos con lógica de dominio compleja, puede evolucionar hacia **arquitectura hexagonal (Ports & Adapters)**.

```
controller/   → Capa de entrada HTTP. Solo delega a servicios, sin lógica de negocio.
service/      → Lógica de aplicación y orquestación.
repository/   → Acceso a datos vía Spring Data JPA.
entity/       → Entidades JPA mapeadas a tablas.
model/ dto/   → Objetos de transferencia (preferir records).
config/       → Beans de configuración de Spring.
```

### 4.2 Reglas por capa

- **Controllers:** no deben contener lógica de negocio ni acceder a repositorios directamente.
- **Services:** son el punto de entrada a la lógica. Deben ser `@Transactional` cuando operan sobre la base de datos.
- **Repositories:** solo interfaces de Spring Data JPA. Las queries complejas van en el repositorio como métodos con `@Query`, no en el servicio.
- **Entities:** solo anotaciones JPA y validaciones. Sin lógica de negocio.

### 4.3 Persistencia de usuarios
El modelo `User` debe ser una entidad JPA persistida en base de datos. No se aceptan usuarios hardcodeados en `UserDetailsService` salvo en entornos de test.

---

## 5. Convenciones de nombres

| Elemento | Convención | Ejemplo |
|---|---|---|
| Clases | PascalCase | `AuthService`, `JwtAuthFilter` |
| Métodos | camelCase | `generateToken()`, `findByUsername()` |
| Records/DTOs | Sufijo descriptivo | `LoginRequest`, `JwtResponse` |
| Entidades JPA | Sin sufijo | `Product`, `User` |
| Tablas BD | snake_case plural | `products`, `users` |
| Variables de entorno | UPPER_SNAKE_CASE | `JWT_SECRET`, `DB_PASSWORD` |

---

## 6. Testing

- Cobertura mínima definida en JaCoCo: **80% instrucciones / 70% ramas**.
- Los tests de integración deben usar un perfil `test` con H2 y `@SpringBootTest`.
- No mockear la base de datos en tests de repositorio — usar H2 real.
- Los tests unitarios de servicios deben mockear dependencias con Mockito.
