# API REST para Generación de JWT con Spring Boot

Esta es una API REST completa para generar y manejar tokens JWT (JSON Web Tokens) usando Spring Boot y Spring Security.

## Características

- ✅ Generación de tokens JWT
- ✅ Autenticación de usuarios
- ✅ Registro de nuevos usuarios
- ✅ Endpoints protegidos
- ✅ Validación de tokens
- ✅ Configuración de seguridad
- ✅ Encriptación de contraseñas con BCrypt

## Tecnologías Utilizadas

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Security**
- **JWT (JSON Web Tokens)**
- **Maven**
- **BCrypt para encriptación**

## Estructura del Proyecto

```
src/
├── main/
│   ├── java/com/example/jwtapi/
│   │   ├── JwtApiApplication.java          # Clase principal de Spring Boot
│   │   ├── config/
│   │   │   └── SecurityConfig.java         # Configuración de seguridad
│   │   ├── controller/
│   │   │   ├── AuthController.java         # Controlador de autenticación
│   │   │   └── ProtectedController.java   # Controlador de endpoints protegidos
│   │   ├── dto/
│   │   │   ├── JwtResponse.java           # DTO para respuesta JWT
│   │   │   └── LoginRequest.java          # DTO para solicitud de login
│   │   ├── model/
│   │   │   └── User.java                  # Modelo de usuario
│   │   └── service/
│   │       ├── AuthService.java           # Servicio de autenticación
│   │       └── JwtService.java            # Servicio JWT
│   └── resources/
│       └── application.properties         # Configuración de la aplicación
└── pom.xml                               # Dependencias de Maven
```

## Instalación y Ejecución

### Prerrequisitos

- Java 17 o superior
- Maven 3.6 o superior

### Pasos para ejecutar

1. **Clonar o descargar el proyecto**

2. **Compilar el proyecto**
   ```bash
   mvn clean compile
   ```

3. **Ejecutar la aplicación**
   ```bash
   mvn spring-boot:run
   ```

4. **La aplicación estará disponible en:**
   ```
   http://localhost:8080
   ```

## Endpoints de la API

### Endpoints Públicos (No requieren autenticación)

#### 1. Probar la API
```http
GET /api/auth/test
```

#### 2. Registrar un nuevo usuario
```http
POST /api/auth/register
Content-Type: application/json

{
    "username": "nuevo_usuario",
    "email": "usuario@ejemplo.com",
    "password": "mi_password123"
}
```

#### 3. Autenticar usuario existente
```http
POST /api/auth/login
Content-Type: application/json

{
    "username": "admin",
    "password": "admin123"
}
```

### Endpoints Protegidos (Requieren token JWT)

#### 4. Obtener perfil del usuario
```http
GET /api/protected/profile
Authorization: Bearer <tu_token_jwt>
```

#### 5. Obtener información del usuario
```http
GET /api/protected/user-info
Authorization: Bearer <tu_token_jwt>
```

## Ejemplos de Uso

### 1. Registrar un nuevo usuario

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "juan_perez",
    "email": "juan@ejemplo.com",
    "password": "mi_password123"
  }'
```

**Respuesta:**
```json
{
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "type": "Bearer",
    "username": "juan_perez",
    "email": "juan@ejemplo.com"
}
```

### 2. Autenticar usuario existente

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'
```

### 3. Acceder a endpoint protegido

```bash
curl -X GET http://localhost:8080/api/protected/profile \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

## Configuración

### Variables de configuración (application.properties)

- `server.port`: Puerto del servidor (por defecto: 8080)
- `jwt.secret`: Clave secreta para firmar los tokens JWT
- `jwt.expiration`: Tiempo de expiración del token en milisegundos (24 horas por defecto)

### Usuario de prueba

La aplicación incluye un usuario de prueba:
- **Username:** `admin`
- **Password:** `admin123`

## Seguridad

- Las contraseñas se encriptan usando BCrypt
- Los tokens JWT tienen un tiempo de expiración configurable
- Los endpoints protegidos requieren un token válido en el header `Authorization`
- La configuración de seguridad está optimizada para APIs REST

## Desarrollo

### Agregar nuevos endpoints protegidos

1. Crear el método en `ProtectedController`
2. Agregar la validación del token JWT
3. Usar `JwtService` para extraer información del token

### Personalizar la configuración JWT

Modifica las propiedades en `application.properties`:
- Cambiar la clave secreta
- Ajustar el tiempo de expiración
- Configurar otros parámetros de seguridad

## Solución de Problemas

### Error de compilación
- Verificar que tienes Java 17 instalado
- Ejecutar `mvn clean install`

### Error de conexión
- Verificar que el puerto 8080 esté disponible
- Cambiar el puerto en `application.properties`

### Error de autenticación
- Verificar que el token JWT sea válido
- Comprobar que el header `Authorization` esté correctamente formateado

## Contribuciones

¡Las contribuciones son bienvenidas! Por favor:

1. Fork el proyecto
2. Crea una rama para tu feature
3. Commit tus cambios
4. Push a la rama
5. Abre un Pull Request

## Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo `LICENSE` para más detalles.
