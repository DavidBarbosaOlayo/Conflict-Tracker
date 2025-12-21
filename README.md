# Conflict Tracker API (Spring Boot)

API REST desarrollada con **Spring Boot 3 + Java 17 + Spring Data JPA** para gestionar información sobre conflictos bélicos.  
Actualmente el proyecto implementa **solo la parte de `Conflict`** (CRUD + filtro por estado) y un **frontend testimonial** para probar la API (frontend por cortesía de ChatGPT).

## Tecnologías
- Java 17
- Spring Boot 3
- Spring Web
- Spring Data JPA (Hibernate)
- H2 Database (desarrollo)
- Maven

## Cómo ejecutar
1. Clona el repositorio y abre el proyecto.
2. Ejecuta la aplicación:
   - IntelliJ: Run `ConflictTrackerApplication`
   - Terminal: `mvn spring-boot:run` (debes tener maven en el path)
3. La app se levanta en `http://localhost:8080`

## Base de datos (H2)
- Consola H2: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`
- User: `sa`
- Password: `password`

El proyecto incluye un `data.sql` con datos iniciales para probar la API.

## Frontend testimonial
Se sirve un `index.html` desde `src/main/resources/static`.

- Abrir: `http://localhost:8080/`
- El frontend consume `GET /api/v1/conflicts` y muestra los resultados en una tabla.
- (Opcional si lo has añadido) permite crear/editar/borrar conflictos usando la API.

## Endpoints implementados (Conflict)
Base path: `/api/v1`

- `GET /conflicts`  
  Lista todos los conflictos.

- `GET /conflicts?status=ACTIVE`  
  Lista conflictos filtrando por estado (`ACTIVE`, `FROZEN`, `ENDED`).

- `GET /conflicts/{id}`  
  Devuelve un conflicto por id.

- `POST /conflicts`  
  Crea un conflicto. Body (JSON):
  ```json
  {
    "name": "Example",
    "startDate": "2020-01-01",
    "status": "ACTIVE",
    "description": "..."
  }
