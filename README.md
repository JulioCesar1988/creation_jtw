# API REST para Generación de JWT con Spring Boot

API REST completa para autenticación con JWT y gestión de productos, construida con Spring Boot 3 y Spring Security 6.

## Características

- Generación y validación de tokens JWT (con `jti` UUID para unicidad garantizada)
- Autenticación y registro de usuarios
- CRUD completo de productos con búsquedas avanzadas
- Endpoints protegidos por JWT
- Encriptación de contraseñas con BCrypt
- Base de datos H2 en memoria con consola web
- Documentación interactiva con Swagger UI
- Soporte Docker y Docker Compose
- CI/CD con GitHub Actions (bloquea merges si los tests fallan)
- Cobertura de tests: **97.9% instrucciones / 95.8% ramas** (verificada con JaCoCo)

## Tecnologías

| Componente | Versión |
|---|---|
| Java | 17 |
| Spring Boot | 3.2.0 |
| Spring Security | 6.x (Jakarta EE) |
| JJWT | 0.12.3 |
| Spring Data JPA + H2 | — |
| SpringDoc OpenAPI | 2.2.0 |
| JaCoCo | 0.8.8 |
| Maven | 3.x |

## Estructura del proyecto

```
.github/workflows/
└── ci.yml                              # Pipeline CI/CD (GitHub Actions)

src/
├── main/java/com/example/jwtapi/
│   ├── config/
│   │   ├── SecurityConfig.java         # Configuración Spring Security 6
│   │   └── OpenApiConfig.java          # Configuración Swagger / OpenAPI
│   ├── controller/
│   │   ├── AuthController.java         # Login y registro
│   │   ├── ProductController.java      # CRUD de productos
│   │   └── ProtectedController.java    # Endpoints que requieren JWT
│   ├── dto/
│   │   ├── LoginRequest.java
│   │   └── JwtResponse.java
│   ├── entity/
│   │   └── Product.java                # Entidad JPA
│   ├── model/
│   │   └── User.java                   # POJO de usuario (no persistido aún)
│   ├── repository/
│   │   └── ProductRepository.java
│   └── service/
│       ├── AuthService.java
│       ├── JwtService.java
│       ├── CustomUserDetailsService.java
│       └── ProductService.java
├── resources/
│   ├── application.properties
│   └── application-docker.properties

src/test/                               # Suite completa: unit + integración
├── java/...                            # 231 tests, 13 clases de test
└── resources/application-test.properties

CONTEXT.md                              # Guía de arquitectura y estilo para IAs
Dockerfile
docker-compose.yml
pom.xml
```

## Instalación y ejecución

### Prerrequisitos

- Java 17+
- Maven 3.6+

### Ejecutar localmente

```bash
# Compilar
mvn clean compile

# Ejecutar tests
mvn test

# Levantar la aplicación
mvn spring-boot:run
```

La aplicación queda disponible en `http://localhost:8080`.

### Ejecutar con Docker

```bash
# Construir imagen y levantar contenedor
docker-compose up --build

# O usar los scripts incluidos
./build-docker.sh        # Solo build
./build-and-push-docker.sh  # Build + push a Docker Hub
```

## Endpoints

### Autenticación (público)

```http
GET  /api/auth/test                # Health check
POST /api/auth/register            # Registrar usuario
POST /api/auth/login               # Login → retorna JWT
```

**Ejemplo de login:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "admin", "password": "admin123"}'
```

**Respuesta:**
```json
{
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "type": "Bearer",
    "username": "admin",
    "email": ""
}
```

### Productos (público)

```http
GET    /api/products                          # Listar todos
GET    /api/products/{id}                     # Obtener por ID
POST   /api/products                          # Crear
PUT    /api/products/{id}                     # Actualizar completo
DELETE /api/products/{id}                     # Eliminar
PATCH  /api/products/{id}/stock               # Actualizar stock
GET    /api/products/search?name=             # Buscar por nombre
GET    /api/products/search/price?minPrice=&maxPrice=
GET    /api/products/search/advanced?name=&maxPrice=
GET    /api/products/stock?minStock=          # Filtrar por stock
GET    /api/products/count?minStock=          # Contar con stock
```

### Endpoints protegidos (requieren JWT)

```http
GET /api/protected/profile      # Perfil del usuario autenticado
GET /api/protected/user-info    # Info básica del usuario
```

```bash
curl -X GET http://localhost:8080/api/protected/profile \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."
```

## Configuración

```properties
# JWT
jwt.secret=<clave-en-base64>   # No commitear valores reales
jwt.expiration=86400000        # 24 horas en ms

# Base de datos H2
spring.datasource.url=jdbc:h2:mem:testdb
spring.h2.console.path=/h2-console

# Swagger UI
springdoc.swagger-ui.path=/swagger-ui.html
```

### Usuario de prueba

- **Username:** `admin`
- **Password:** `admin123`

## Documentación interactiva

Con la aplicación corriendo, Swagger UI está disponible en:

```
http://localhost:8080/swagger-ui.html
```

La consola H2 está en:

```
http://localhost:8080/h2-console
```

## Tests y cobertura

```bash
# Ejecutar tests
mvn test

# Generar reporte HTML de cobertura
mvn jacoco:report
# Reporte en: target/site/jacoco/index.html

# Verificar umbrales de cobertura (falla el build si no se cumplen)
mvn verify
```

Umbrales configurados en JaCoCo:

| Contador | Mínimo requerido | Estado actual |
|---|---|---|
| Instrucciones | 80% | **97.9%** |
| Ramas | 70% | **95.8%** |

La suite incluye:
- Tests unitarios por capa (controller, service, repository, dto, entity)
- Tests de integración con H2 real (`@DataJpaTest`)
- Tests de seguridad con Spring Security real (`@Import(SecurityConfig.class)`)

## CI/CD

El workflow `.github/workflows/ci.yml` se dispara en cada `push` y `pull request` a `main`:

1. Checkout del código
2. Setup Java 17 (Temurin) con caché de dependencias Maven
3. `mvn compile` — falla si hay errores de compilación
4. `mvn verify` — ejecuta tests y valida umbrales de cobertura JaCoCo
5. Sube el reporte HTML de cobertura como artefacto (15 días de retención)

Para bloquear merges automáticamente en GitHub: **Settings → Branches → Branch protection rules → Require status checks to pass → `Compile & Test`**.

## Guía de arquitectura

El archivo `CONTEXT.md` en la raíz define las reglas de estilo, seguridad y arquitectura que deben respetarse al modificar el proyecto (inyección por constructor, uso de records, prohibición de `WebSecurityConfigurerAdapter`, etc.). Está pensado para ser leído por desarrolladores e IAs antes de escribir código.

## Solución de problemas

**Error de compilación:** verificar Java 17 instalado → `java -version`

**Puerto ocupado:** cambiar `server.port` en `application.properties`

**Token rechazado:** verificar que el header sea `Authorization: Bearer <token>` (sin espacios extra ni token vacío)

**Tests fallan localmente:** ejecutar `mvn clean test` para descartar clases compiladas obsoletas

## Contribuciones

1. Fork el proyecto
2. Crear rama para el feature
3. Asegurarse de que `mvn verify` pasa (tests + cobertura)
4. Abrir un Pull Request — el CI validará automáticamente

## Licencia

MIT
