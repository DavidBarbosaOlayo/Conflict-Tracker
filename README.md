# Conflict Tracker

Proyecto de clase sobre conflictos internacionales.

La aplicación permite:
- ver un listado de conflictos
- filtrar por nombre, estado y país
- entrar al detalle de cada conflicto
- crear nuevos conflictos
- ver un gráfico de estados y un mapa simple

## Qué se ha usado
- Spring Boot para el backend
- Vue 3 para el frontend
- H2 como base de datos por defecto

## Cómo ejecutarlo

Arrancar todo el proyecto:

```bash
./mvnw spring-boot:run
```

Después abrir en el navegador:
- `http://localhost:8080/`

## Rutas principales
- `/` inicio de la aplicación
- `/conflicts/{id}` detalle de un conflicto
- `/api/v1/conflicts` API en JSON

## Frontend en desarrollo

Si quieres abrir solo el frontend con recarga automática:

```bash
cd frontend
npm install
npm run dev
```

Y abrir:
- `http://localhost:5173/`

## Nota
El proyecto usa H2 por defecto, así que no hace falta configurar nada raro para probarlo.
