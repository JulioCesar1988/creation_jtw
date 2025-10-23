# 🐳 Configuración Docker Hub

## 📍 Ubicación de Imágenes Docker

### **Almacenamiento Local:**

#### **Windows:**
```
C:\Users\[usuario]\AppData\Local\Docker\wsl\data\ext4.vhdx
```

#### **Linux:**
```
/var/lib/docker/
```

#### **macOS:**
```
~/Library/Containers/com.docker.docker/Data/vms/0/data/
```

## 🚀 Subir Imagen a Docker Hub

### **1. Crear cuenta en Docker Hub:**
- Ve a: https://hub.docker.com
- Crea una cuenta gratuita
- Verifica tu email

### **2. Loguearse desde terminal:**
```bash
docker login
```

### **3. Usar scripts automatizados:**

#### **Windows (PowerShell):**
```powershell
.\build-and-push-docker.ps1 -DockerHubUsername "tuusuario"
```

#### **Linux/Mac (Bash):**
```bash
./build-and-push-docker.sh -u tuusuario
```

### **4. Comandos manuales:**

#### **Construir imagen:**
```bash
mvn clean package -DskipTests
docker build -t jwt-api:latest .
```

#### **Etiquetar para Docker Hub:**
```bash
docker tag jwt-api:latest tuusuario/jwt-api:latest
```

#### **Subir a Docker Hub:**
```bash
docker push tuusuario/jwt-api:latest
```

## 🔍 Gestionar Imágenes Locales

### **Ver imágenes:**
```bash
docker images
docker images jwt-api
```

### **Ver tamaño:**
```bash
docker system df
```

### **Eliminar imagen:**
```bash
docker rmi jwt-api:latest
docker rmi tuusuario/jwt-api:latest
```

### **Ver detalles:**
```bash
docker inspect jwt-api:latest
```

## 📦 Compartir Imágenes

### **1. Docker Hub (Público):**
```bash
docker push tuusuario/jwt-api:latest
```

### **2. Registro Privado:**
```bash
docker tag jwt-api:latest mi-registro.com/jwt-api:latest
docker push mi-registro.com/jwt-api:latest
```

### **3. Exportar/Importar:**
```bash
# Exportar
docker save jwt-api:latest > jwt-api.tar

# Importar
docker load < jwt-api.tar
```

## 🌐 URLs de la Imagen

Una vez subida a Docker Hub:

- **Docker Hub:** https://hub.docker.com/r/tuusuario/jwt-api
- **Pull Command:** `docker pull tuusuario/jwt-api:latest`
- **Run Command:** `docker run -p 8080:8080 tuusuario/jwt-api:latest`

## 🔧 Configuración Avanzada

### **Multi-archivo (Multi-platform):**
```bash
docker buildx create --use
docker buildx build --platform linux/amd64,linux/arm64 -t tuusuario/jwt-api:latest --push .
```

### **Tags múltiples:**
```bash
docker tag jwt-api:latest tuusuario/jwt-api:v1.0.0
docker tag jwt-api:latest tuusuario/jwt-api:stable
docker push tuusuario/jwt-api:v1.0.0
docker push tuusuario/jwt-api:stable
```

## 📊 Monitoreo

### **Ver imágenes en Docker Hub:**
- Ve a tu perfil en Docker Hub
- Sección "Repositories"
- Ver estadísticas de descarga

### **Logs de push:**
```bash
docker push tuusuario/jwt-api:latest
```

## 🛠️ Troubleshooting

### **Error de autenticación:**
```bash
docker logout
docker login
```

### **Error de permisos:**
- Verificar que tienes permisos de escritura en el repositorio
- Verificar que el nombre del repositorio es correcto

### **Imagen muy grande:**
- Usar `.dockerignore` para excluir archivos innecesarios
- Usar imágenes base más pequeñas
- Multi-stage builds

