# --- BUILD STAGE ---
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copier tout le backend
COPY . .

# Build du JAR, sans tests pour accélérer
RUN mvn clean package -DskipTests

# --- RUN STAGE ---
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copier le JAR généré et le renommer app.jar
COPY --from=build /app/target/*.jar app.jar

# Render fournit le port via la variable d'environnement PORT
ENV PORT=8080
EXPOSE $PORT

# Lancer le backend sur le port fourni par Render
CMD ["sh", "-c", "java -jar app.jar --server.port=$PORT"]