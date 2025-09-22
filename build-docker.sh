#!/bin/bash

echo "🚀 Construyendo aplicación JWT API para Docker..."

# Limpiar y compilar el proyecto
echo "📦 Compilando proyecto..."
mvn clean package -DskipTests

# Verificar que el WAR se generó correctamente
if [ ! -f "target/jwt-api-0.0.1-SNAPSHOT.war" ]; then
    echo "❌ Error: No se pudo generar el archivo WAR"
    exit 1
fi

echo "✅ WAR generado exitosamente: target/jwt-api-0.0.1-SNAPSHOT.war"

# Construir imagen Docker
echo "🐳 Construyendo imagen Docker..."
docker build -t jwt-api:latest .

if [ $? -eq 0 ]; then
    echo "✅ Imagen Docker construida exitosamente: jwt-api:latest"
    echo ""
    echo "📋 Comandos disponibles:"
    echo "  docker run -p 8080:8080 jwt-api:latest"
    echo "  docker-compose up"
    echo ""
    echo "🌐 URLs de acceso:"
    echo "  API: http://localhost:8080/api/auth/test"
    echo "  Swagger: http://localhost:8080/swagger-ui.html"
    echo "  H2 Console: http://localhost:8080/h2-console"
else
    echo "❌ Error al construir la imagen Docker"
    exit 1
fi
