# Script de PowerShell para construir la aplicación Docker
Write-Host "🚀 Construyendo aplicación JWT API para Docker..." -ForegroundColor Green

# Limpiar y compilar el proyecto
Write-Host "📦 Compilando proyecto..." -ForegroundColor Yellow
mvn clean package -DskipTests

# Verificar que el WAR se generó correctamente
if (-not (Test-Path "target/jwt-api-0.0.1-SNAPSHOT.war")) {
    Write-Host "❌ Error: No se pudo generar el archivo WAR" -ForegroundColor Red
    exit 1
}

Write-Host "✅ WAR generado exitosamente: target/jwt-api-0.0.1-SNAPSHOT.war" -ForegroundColor Green

# Construir imagen Docker
Write-Host "🐳 Construyendo imagen Docker..." -ForegroundColor Yellow
docker build -t jwt-api:latest .

if ($LASTEXITCODE -eq 0) {
    Write-Host "✅ Imagen Docker construida exitosamente: jwt-api:latest" -ForegroundColor Green
    Write-Host ""
    Write-Host "📋 Comandos disponibles:" -ForegroundColor Cyan
    Write-Host "  docker run -p 8080:8080 jwt-api:latest" -ForegroundColor White
    Write-Host "  docker-compose up" -ForegroundColor White
    Write-Host ""
    Write-Host "🌐 URLs de acceso:" -ForegroundColor Cyan
    Write-Host "  API: http://localhost:8080/api/auth/test" -ForegroundColor White
    Write-Host "  Swagger: http://localhost:8080/swagger-ui.html" -ForegroundColor White
    Write-Host "  H2 Console: http://localhost:8080/h2-console" -ForegroundColor White
} else {
    Write-Host "❌ Error al construir la imagen Docker" -ForegroundColor Red
    exit 1
}
