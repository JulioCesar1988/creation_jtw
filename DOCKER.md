# 🐳 Dockerización de JWT API

Este documento explica cómo contenerizar y ejecutar la aplicación JWT API usando Docker.

## 📋 Prerrequisitos

- Docker instalado
- Docker Compose instalado
- Maven instalado
- Java 17 instalado

## 🚀 Construcción y Ejecución

### Opción 1: Usando Scripts Automatizados

#### Windows (PowerShell):
```powershell
.\build-docker.ps1
```

#### Linux/Mac (Bash):
```bash
./build-docker.sh
```

### Opción 2: Pasos Manuales

#### 1. Compilar el proyecto:
```bash
mvn clean package -DskipTests
```

#### 2. Construir imagen Docker:
```bash
docker build -t jwt-api:latest .
```

#### 3. Ejecutar contenedor:
```bash
docker run -p 8080:8080 jwt-api:latest
```

### Opción 3: Usando Docker Compose

```bash
docker-compose up
```

## 🌐 URLs de Acceso

Una vez que el contenedor esté ejecutándose:

- **API Test:** http://localhost:8080/api/auth/test
- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **H2 Console:** http://localhost:8080/h2-console
- **API Docs:** http://localhost:8080/api-docs

## 🔧 Configuración

### Variables de Entorno

El contenedor utiliza las siguientes variables de entorno:

- `JAVA_OPTS`: Opciones de JVM (por defecto: `-Xmx512m -Xms256m`)
- `SPRING_PROFILES_ACTIVE`: Perfil activo (por defecto: `docker`)

### Personalizar Configuración

Puedes modificar las variables de entorno al ejecutar el contenedor:

```bash
docker run -p 8080:8080 \
  -e JAVA_OPTS="-Xmx1024m -Xms512m" \
  -e SPRING_PROFILES_ACTIVE=production \
  jwt-api:latest
```

## 📊 Monitoreo

### Health Check

El contenedor incluye un health check que verifica:
- URL: `http://localhost:8080/api/auth/test`
- Intervalo: 30 segundos
- Timeout: 10 segundos
- Reintentos: 3

### Verificar Estado

```bash
docker ps
docker logs jwt-api-container
```

## 🛠️ Comandos Útiles

### Ver logs del contenedor:
```bash
docker logs -f jwt-api-container
```

### Acceder al contenedor:
```bash
docker exec -it jwt-api-container /bin/bash
```

### Detener el contenedor:
```bash
docker stop jwt-api-container
```

### Eliminar imagen:
```bash
docker rmi jwt-api:latest
```

## 🔍 Troubleshooting

### Puerto ya en uso:
```bash
# Cambiar puerto
docker run -p 8081:8080 jwt-api:latest
```

### Problemas de memoria:
```bash
# Aumentar memoria JVM
docker run -p 8080:8080 -e JAVA_OPTS="-Xmx1024m -Xms512m" jwt-api:latest
```

### Ver logs detallados:
```bash
docker logs jwt-api-container
```

## 📦 Estructura del Contenedor

```
/app/
├── jwt-api.war          # Aplicación WAR
└── Dockerfile          # Configuración Docker
```

## 🏗️ Arquitectura

- **Base Image:** OpenJDK 17 JDK Slim
- **Puerto:** 8080
- **Tipo:** WAR ejecutable
- **Base de Datos:** H2 en memoria
- **Perfil:** docker

## 📝 Notas Importantes

1. La base de datos H2 se reinicia con cada ejecución del contenedor
2. Los datos no persisten entre reinicios del contenedor
3. Para producción, considera usar una base de datos externa
4. El contenedor incluye health checks para monitoreo
