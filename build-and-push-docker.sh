#!/bin/bash

# Script para construir y subir imagen a Docker Hub
DOCKER_HUB_USERNAME=""
TAG="latest"

# Función para mostrar ayuda
show_help() {
    echo "Uso: $0 -u <dockerhub_username> [-t <tag>]"
    echo ""
    echo "Opciones:"
    echo "  -u, --username    Usuario de Docker Hub (requerido)"
    echo "  -t, --tag        Tag de la imagen (default: latest)"
    echo "  -h, --help       Mostrar esta ayuda"
    echo ""
    echo "Ejemplo:"
    echo "  $0 -u miusuario"
    echo "  $0 -u miusuario -t v1.0.0"
}

# Procesar argumentos
while [[ $# -gt 0 ]]; do
    case $1 in
        -u|--username)
            DOCKER_HUB_USERNAME="$2"
            shift 2
            ;;
        -t|--tag)
            TAG="$2"
            shift 2
            ;;
        -h|--help)
            show_help
            exit 0
            ;;
        *)
            echo "Opción desconocida: $1"
            show_help
            exit 1
            ;;
    esac
done

# Verificar que se proporcionó el username
if [ -z "$DOCKER_HUB_USERNAME" ]; then
    echo "❌ Error: Debes proporcionar el nombre de usuario de Docker Hub"
    show_help
    exit 1
fi

echo "🚀 Construyendo y subiendo JWT API a Docker Hub..."

# Verificar que Docker esté ejecutándose
if ! docker version > /dev/null 2>&1; then
    echo "❌ Error: Docker no está ejecutándose. Por favor inicia Docker."
    exit 1
fi

# Limpiar y compilar el proyecto
echo "📦 Compilando proyecto..."
mvn clean package -DskipTests

if [ ! -f "target/jwt-api-0.0.1-SNAPSHOT.war" ]; then
    echo "❌ Error: No se pudo generar el archivo WAR"
    exit 1
fi

echo "✅ WAR generado exitosamente"

# Construir imagen local
echo "🐳 Construyendo imagen Docker local..."
docker build -t jwt-api:$TAG .

if [ $? -ne 0 ]; then
    echo "❌ Error al construir la imagen Docker"
    exit 1
fi

# Etiquetar para Docker Hub
IMAGE_NAME="$DOCKER_HUB_USERNAME/jwt-api:$TAG"
echo "🏷️ Etiquetando imagen para Docker Hub: $IMAGE_NAME"
docker tag jwt-api:$TAG $IMAGE_NAME

# Subir a Docker Hub
echo "⬆️ Subiendo imagen a Docker Hub..."
docker push $IMAGE_NAME

if [ $? -eq 0 ]; then
    echo "✅ Imagen subida exitosamente a Docker Hub!"
    echo ""
    echo "📋 Información de la imagen:"
    echo "  Nombre: $IMAGE_NAME"
    echo "  Docker Hub: https://hub.docker.com/r/$DOCKER_HUB_USERNAME/jwt-api"
    echo ""
    echo "🚀 Comando para ejecutar:"
    echo "  docker run -p 8080:8080 $IMAGE_NAME"
    echo ""
    echo "🌐 URLs de acceso:"
    echo "  API: http://localhost:8080/api/auth/test"
    echo "  Swagger: http://localhost:8080/swagger-ui.html"
    echo "  H2 Console: http://localhost:8080/h2-console"
else
    echo "❌ Error al subir la imagen a Docker Hub"
    echo "💡 Asegúrate de estar logueado: docker login"
    exit 1
fi

