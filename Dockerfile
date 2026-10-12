# ── Etapa 1: Build con Maven ──────────────────────────────────
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copia primero el pom.xml para aprovechar la caché de capas Docker
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia el resto del código y compila (sin tests, los tests corren en CI)
COPY src ./src
RUN mvn clean package -DskipTests -B

# ── Etapa 2: Runtime ligero ───────────────────────────────────
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copia el JAR desde la etapa de build
COPY --from=build /app/target/lab1back-arquisoft.jar lab1back-arquisoft.jar

EXPOSE 8088

ENTRYPOINT ["java", "-jar", "lab1back-arquisoft.jar"]
