# Script para construir y subir imagen a Docker Hub
param(
    [Parameter(Mandatory=$true)]
    [string]$DockerHubUsername,
    
    [Parameter(Mandatory=$false)]
    [string]$Tag = "latest"
)

Write-Host "🚀 Construyendo y subiendo JWT API a Docker Hub..." -ForegroundColor Green

# Verificar que Docker esté ejecutándose
try {
    docker version | Out-Null
} catch {
    Write-Host "❌ Error: Docker no está ejecutándose. Por favor inicia Docker Desktop." -ForegroundColor Red
    exit 1
}

# Limpiar y compilar el proyecto
Write-Host "📦 Compilando proyecto..." -ForegroundColor Yellow
mvn clean package -DskipTests

if (-not (Test-Path "target/jwt-api-0.0.1-SNAPSHOT.war")) {
    Write-Host "❌ Error: No se pudo generar el archivo WAR" -ForegroundColor Red
    exit 1
}

Write-Host "✅ WAR generado exitosamente" -ForegroundColor Green

# Construir imagen local
Write-Host "🐳 Construyendo imagen Docker local..." -ForegroundColor Yellow
docker build -t jwt-api:$Tag .

if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Error al construir la imagen Docker" -ForegroundColor Red
    exit 1
}

# Etiquetar para Docker Hub
$ImageName = "$DockerHubUsername/jwt-api:$Tag"
Write-Host "🏷️ Etiquetando imagen para Docker Hub: $ImageName" -ForegroundColor Yellow
docker tag jwt-api:$Tag $ImageName

# Subir a Docker Hub
Write-Host "⬆️ Subiendo imagen a Docker Hub..." -ForegroundColor Yellow
docker push $ImageName

if ($LASTEXITCODE -eq 0) {
    Write-Host "✅ Imagen subida exitosamente a Docker Hub!" -ForegroundColor Green
    Write-Host ""
    Write-Host "📋 Información de la imagen:" -ForegroundColor Cyan
    Write-Host "  Nombre: $ImageName" -ForegroundColor White
    Write-Host "  Docker Hub: https://hub.docker.com/r/$DockerHubUsername/jwt-api" -ForegroundColor White
    Write-Host ""
    Write-Host "🚀 Comando para ejecutar:" -ForegroundColor Cyan
    Write-Host "  docker run -p 8080:8080 $ImageName" -ForegroundColor White
    Write-Host ""
    Write-Host "🌐 URLs de acceso:" -ForegroundColor Cyan
    Write-Host "  API: http://localhost:8080/api/auth/test" -ForegroundColor White
    Write-Host "  Swagger: http://localhost:8080/swagger-ui.html" -ForegroundColor White
    Write-Host "  H2 Console: http://localhost:8080/h2-console" -ForegroundColor White
} else {
    Write-Host "❌ Error al subir la imagen a Docker Hub" -ForegroundColor Red
    Write-Host "💡 Asegúrate de estar logueado: docker login" -ForegroundColor Yellow
    exit 1
}

