# ms-user

Microservicio encargado de la gestión de usuarios del reto Plazoleta.

## HU implementada en este paso

- HU1: crear propietario

## Endpoint disponible

- `POST /users/owners`

## Ejecución local

```bash
bash gradlew bootRun
```

## Documentación OpenAPI

Swagger UI local:

```text
http://localhost:8082/swagger-ui.html
```

OpenAPI JSON local:

```text
http://localhost:8082/v3/api-docs
```

## Validación

```bash
bash gradlew clean build
bash gradlew test
bash gradlew jacocoTestReport
```

## Cobertura

El reporte HTML de JaCoCo queda en:

```text
build/reports/jacoco/test/html/index.html
```

## Dependencias compartidas

Este microservicio consume `plazoleta-common-lib` desde `mavenLocal()`.
