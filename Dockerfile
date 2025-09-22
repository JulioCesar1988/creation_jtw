# Usar imagen base de OpenJDK 17
FROM openjdk:17-jdk-slim

# Establecer directorio de trabajo
WORKDIR /app

# Instalar herramientas necesarias
RUN apt-get update && apt-get install -y \
    curl \
    && rm -rf /var/lib/apt/lists/*

# Copiar el archivo WAR
COPY target/jwt-api-0.0.1-SNAPSHOT.war /app/jwt-api.war

# Exponer puerto 8080
EXPOSE 8080

# Variables de entorno
ENV JAVA_OPTS="-Xmx512m -Xms256m"
ENV SPRING_PROFILES_ACTIVE=docker

# Comando para ejecutar la aplicación
CMD ["sh", "-c", "java $JAVA_OPTS -jar jwt-api.war"]
