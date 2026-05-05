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
- H2 como base de datos por defecto en local
- PostgreSQL (Supabase) en producción
- Render para hostear el backend
- Vercel para hostear el frontend

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

## Despliegue en la nube

Para la práctica de despliegue el proyecto se ha partido en tres capas:

- Base de datos: PostgreSQL en Supabase
- Backend: Spring Boot en Render (con Docker)
- Frontend: Vue 3 en Vercel

URLs:
- Backend: https://conflict-tracker-api-zzui.onrender.com
- API: https://conflict-tracker-api-zzui.onrender.com/api/v1/conflicts
- Frontend: (pendiente de subir a Vercel)

Esquema:

```
Navegador  ->  Vercel (Vue)  ->  Render (Spring Boot)  ->  Supabase (Postgres)
```

## Variables de entorno

### Backend (Render)
- `SPRING_PROFILES_ACTIVE=prod`
- `DB_URL` cadena JDBC de Supabase (Session Pooler, puerto 5432)
- `DB_USERNAME` usuario de Supabase (`postgres.<project-ref>`)
- `DB_PASSWORD` contraseña de la base de datos
- `SQL_INIT_MODE=never`. Sólo el primer arranque se puso en `always` para que el `data.sql` poblara la base de datos.
- `CORS_ALLOWED_ORIGINS` URL del frontend en Vercel, separadas por comas si son varias.

### Frontend (Vercel)
- `VITE_API_URL=https://conflict-tracker-api-zzui.onrender.com/api/v1`

En local hay un `frontend/.env` con `VITE_API_URL=/api/v1` para que en `npm run dev` siga funcionando el proxy de Vite contra el backend local.

## Cambios respecto a la versión anterior

### Backend
- Nuevo perfil `prod` (`application-prod.properties`) que lee `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `CORS_ALLOWED_ORIGINS` y `SQL_INIT_MODE` desde variables de entorno. Antes la conexión a Postgres estaba a `localhost:5432`, lo cual obviamente no vale en Render.
- Clase `WebCorsConfig` que registra el CORS sobre `/api/**` leyendo los orígenes permitidos de propiedades. Sin esto la SPA en Vercel no podía llamar al backend (error de CORS por origen distinto).
- `Dockerfile` multi-stage (Eclipse Temurin 17). Render lo detecta solo y construye la imagen.

### Frontend
- `frontend/src/services/api.js` ahora hace `import.meta.env.VITE_API_URL || '/api/v1'`. Antes el `API_BASE` estaba fijo en `/api/v1`, así que en local con proxy iba bien pero al desplegar en Vercel no encontraba el backend.
- `frontend/vite.config.js` ahora hace `build` a `./dist`. Antes el build se copiaba a `src/main/resources/static` del backend para servirlo todo desde Spring Boot, lo cual rompe la idea de tres capas separadas.
- `frontend/vercel.json` con un rewrite SPA. Sin esto, al refrescar `/conflicts/3` Vercel devolvía 404 porque no existe ese fichero estático.
