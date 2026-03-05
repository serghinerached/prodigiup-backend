# Build stage
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
# Render fournit le port via la variable d'environnement PORT
ENV PORT=8080
EXPOSE $PORT
CMD ["sh", "-c", "java -jar demo-0.0.1-SNAPSHOT.jar --server.port=$PORT"]