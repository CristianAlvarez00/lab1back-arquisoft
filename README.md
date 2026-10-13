# lab1-arquisoft 🏦

[![CI/CD Pipeline](https://github.com/CristianAlvarez00/lab1back-arquisoft/actions/workflows/build.yml/badge.svg)](https://github.com/CristianAlvarez00/lab1back-arquisoft/actions/workflows/build.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=CristianAlvarez00_lab1back-arquisoft&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=CristianAlvarez00_lab1back-arquisoft)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=CristianAlvarez00_lab1back-arquisoft&metric=coverage)](https://sonarcloud.io/summary/new_code?id=CristianAlvarez00_lab1back-arquisoft)
[![Known Vulnerabilities](https://snyk.io/test/github/CristianAlvarez00/lab1back-arquisoft/badge.svg)](https://snyk.io/test/github/CristianAlvarez00/lab1back-arquisoft)
[![Docker Hub](https://img.shields.io/docker/pulls/cristianalvarez00/lab1back-arquisoft?label=Docker%20Hub)](https://hub.docker.com/r/cristianalvarez00/lab1back-arquisoft)

API REST de un sistema bancario simple desarrollado con **Spring Boot 4** y desplegado automáticamente en la nube mediante GitHub Actions.

## Stack tecnológico

- **Java 17** + **Spring Boot 4**
- **JPA / MariaDB** (producción) · **H2** (tests)
- **MapStruct** + **Lombok**
- **JaCoCo** para cobertura de código
- **SonarCloud** para análisis de calidad
- **Snyk** para detección de vulnerabilidades
- **Docker Hub** como registro de imágenes
- **Render** como plataforma de despliegue en la nube

## Pipeline CI/CD

```
push / PR  →  Build & Tests  →  SonarCloud  →  Docker Build & Push  →  Deploy to Render
```

## Cómo ejecutar localmente

```bash
git clone https://github.com/CristianAlvarez00/lab1back-arquisoft.git
cd lab1back-arquisoft
mvn clean verify
```

## Ejecutar con Docker

```bash
docker pull cristianalvarez00/lab1back-arquisoft:latest
docker run -p 8088:8088 cristianalvarez00/lab1back-arquisoft:latest
```

## Estructura del proyecto

| Carpeta | Contenido |
|---------|-----------|
| `src/main/java` | Código fuente principal (controllers, services, repositories) |
| `src/test/java` | Pruebas unitarias e integración |
| `.github/workflows` | Pipeline de CI/CD |
