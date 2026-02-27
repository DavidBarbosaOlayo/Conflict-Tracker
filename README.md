# Conflict Tracker API

API REST desarrollada con Spring Boot 3 para gestionar conflictos bélicos, facciones, países y eventos clave.

## Tecnologías
- Java 17+
- Spring Boot 3
- Spring Web
- Spring Data JPA (Hibernate)
- H2 (desarrollo)
- PostgreSQL (perfil de despliegue)
- Thymeleaf (vista web básica)
- Maven

## Ejecutar el proyecto
1. Compilar:
```bash
./mvnw clean compile
```

2. Ejecutar en desarrollo (H2):
```bash
./mvnw spring-boot:run
```

3. Ejecutar con PostgreSQL:
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=postgres
```

## Base de datos
### H2 (por defecto)
- Consola: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:conflictdb`
- Usuario: `sa`
- Contraseña: `password`

### PostgreSQL
Configura las credenciales en `src/main/resources/application-postgres.properties`.

## API REST
Base path: `/api/v1`

### Conflicts
- `GET /conflicts`
- `GET /conflicts/{id}`
- `POST /conflicts`
- `PUT /conflicts/{id}`
- `DELETE /conflicts/{id}`
- `GET /conflicts?status=ACTIVE`

### Factions
- `GET /factions`
- `GET /factions/{id}`
- `POST /factions`
- `PUT /factions/{id}`
- `DELETE /factions/{id}`

### Events
- `GET /events`
- `GET /events/{id}`
- `POST /events`
- `PUT /events/{id}`
- `DELETE /events/{id}`

### Consulta avanzada
- `GET /countries/{code}/conflicts`

## Ejemplos con curl
Crear conflicto:
```bash
curl -X POST http://localhost:8080/api/v1/conflicts \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Example Conflict",
    "startDate": "2024-01-01",
    "status": "ACTIVE",
    "description": "Conflicto de prueba",
    "countryCodes": ["UKR", "RUS"]
  }'
```

Filtrar conflictos activos:
```bash
curl "http://localhost:8080/api/v1/conflicts?status=ACTIVE"
```

Conflictos por país:
```bash
curl "http://localhost:8080/api/v1/countries/UKR/conflicts"
```

## Vista web (Thymeleaf)
- `GET /web/conflicts`
- Muestra una tabla con nombre, fecha de inicio, estado y descripción corta.
- Permite filtrar por estado.
