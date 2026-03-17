# AGENTS.md

## Propósito
Este repositorio implementa gestión de usuarios del reto plazoleta.

## Responsabilidades
- crear propietario
- crear empleado
- gestionar cliente
- asociar usuarios con roles y restaurante cuando aplique

## Stack obligatorio
- Java 21
- Gradle
- Spring Boot
- Spring Security
- Spring Web
- Validation
- Actuator
- OpenAPI
- JaCoCo
- JUnit 5
- H2 para pruebas
- MySQL como base relacional
- MapStruct
- Lombok

## Reglas obligatorias
- Explicar en español qué se hará antes de tocar código.
- Preguntar antes de ejecutar el siguiente paso.
- No continuar hasta validación del usuario.
- Código, logs y nombres técnicos en inglés.
- JavaDoc de clases en español.
- README y documentación interna en español.
- OpenAPI obligatorio.
- Entregar cURL de cada endpoint.
- Toda configuración debe ir en `application.properties` y `@ConfigurationProperties`.
- Reutilizar `plazoleta-common-lib` desde `mavenLocal()`.
- No duplicar seguridad, errores ni utilidades comunes.
- El formato de error debe ser el estándar compartido.
- La cobertura del reporte JaCoCo debe ser del 100% en el alcance acordado del paso.
- Mientras no exista SonarQube o SonarCloud configurado, validar calidad con criterio equivalente a SonarLint mediante build, test, cobertura y revisión estática local.

## Reglas de arquitectura
- Arquitectura hexagonal.
- Base package: `com.plazoleta.user`.
- DTO solo en infraestructura.
- Application usa command/query/response.
- Un UseCase por operación.
- Un Port por agregado.
- Dominio rico.
- MapStruct.
- Lombok.
- Reutilizar `plazoleta-common-lib` para errores y seguridad.
- No duplicar clases comunes.

## Endpoints esperados
- POST /users/owners
- POST /users/employees
- POST /users/customers
- GET /users/{id}

## Definiciones cerradas para HU1
- `email` único global.
- `documentId` único global.
- `birthDate` en formato `yyyy-MM-dd`.
- El rol `OWNER` se asigna automáticamente.
- La autorización real por JWT se cierra en HU5, pero el servicio debe quedar preparado para integrarse con `plazoleta-common-lib`.

## Antes de implementar preguntar si falta definición sobre
- datos obligatorios por tipo de usuario
- reglas de unicidad
- relación con restaurante
- validaciones de documento, email y teléfono

## Al terminar un cambio
- listar archivos modificados
- indicar cómo probar
- entregar cURLs
- entregar comandos de build, test y jacoco
- indicar la ruta del reporte HTML de JaCoCo
- esperar validación
