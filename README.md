# ms-user

Microservicio encargado de la gestión de usuarios del reto Plazoleta.

## HUs implementadas

- HU1: crear propietario
- HU5: autenticación y autorización por roles
- HU6: crear empleado
- HU8: crear cliente

## Endpoints disponibles

- `POST /users/owners`
- `POST /users/employees`
- `POST /users/customers`
- `GET /users/{id}`
- `GET /users/internal/{id}`
- `GET /users/internal/authentication?email=...`

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

Actuator local:

```text
http://localhost:8082/actuator/health
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
