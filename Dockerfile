# ---- Build stage (uses Maven + JDK 21) ----
FROM maven:3.9.8-eclipse-temurin-21 AS build
WORKDIR /app

# Copy only what we need, then build
COPY pom.xml .
COPY src ./src
RUN mvn -q -DskipTests package

# ---- Run stage (small JRE-only image) ----
FROM eclipse-temurin:21-jre
WORKDIR /app

# Render provides PORT env var. Spring will bind to it via -Dserver.port
ENV PORT=8080
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
CMD ["sh","-c","java -Dserver.port=${PORT} -jar app.jar"]
