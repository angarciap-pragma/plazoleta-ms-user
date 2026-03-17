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
