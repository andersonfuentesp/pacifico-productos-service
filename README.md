# Productos Service

Microservicio reactivo para la gestión de un catálogo de productos. Construido con
Spring WebFlux y R2DBC sobre una base de datos H2 en memoria.

## Stack

- Java 17
- Spring Boot 3.5.14
- Spring WebFlux
- Spring Data R2DBC
- H2 (driver R2DBC)
- JUnit 5 + Mockito + StepVerifier
- JaCoCo
- Docker / Docker Compose

## Requisitos previos

- JDK 17
- Maven 3.9+ (o usar el wrapper `./mvnw`)
- Docker y Docker Compose (para ejecución en contenedor)

## Cómo ejecutar con Docker

Levantar toda la solución con un solo comando:

```bash
docker compose up --build
```

El servicio queda disponible en `http://localhost:8080`.

Para detenerlo:

```bash
docker compose down
```

## Cómo ejecutar localmente (sin Docker)

```bash
./mvnw spring-boot:run
```

## Tests y reporte de cobertura

Ejecutar las pruebas:

```bash
./mvnw test
```

El reporte de cobertura de JaCoCo se genera en:

```
target/site/jacoco/index.html
```

La verificación de cobertura mínima (70% en la capa de servicios) corre con:

```bash
./mvnw verify
```

## Documentación de la API

Con el servicio levantado, Swagger UI está disponible en:

```
http://localhost:8080/swagger-ui.html
```

## Endpoints

Base URL: `/api/v1/productos`

| Método | Ruta    | Descripción                | Código éxito |
|--------|---------|----------------------------|--------------|
| GET    | `/`     | Lista todos los productos  | 200          |
| GET    | `/{id}` | Obtiene un producto por id | 200          |
| POST   | `/`     | Crea un producto           | 201          |
| PUT    | `/{id}` | Actualiza un producto      | 200          |
| DELETE | `/{id}` | Elimina un producto        | 204          |

## Ejemplos de consumo (cURL)

Crear un producto:

```bash
curl -X POST http://localhost:8080/api/v1/productos \
  -H "Content-Type: application/json" \
  -d '{
    "sku": "SKU-001",
    "nombre": "Laptop Lenovo ThinkPad",
    "descripcion": "Equipo corporativo 16GB RAM",
    "precio": 4200.00,
    "stock": 15
  }'
```

Listar todos:

```bash
curl http://localhost:8080/api/v1/productos
```

Obtener por id:

```bash
curl http://localhost:8080/api/v1/productos/1
```

Actualizar:

```bash
curl -X PUT http://localhost:8080/api/v1/productos/1 \
  -H "Content-Type: application/json" \
  -d '{
    "sku": "SKU-001",
    "nombre": "Laptop Lenovo ThinkPad X1",
    "descripcion": "Equipo corporativo 32GB RAM",
    "precio": 5100.00,
    "stock": 8
  }'
```

Eliminar:

```bash
curl -X DELETE http://localhost:8080/api/v1/productos/1
```

## Manejo de errores

Las respuestas de error siguen un formato consistente:

```json
{
  "timestamp": "2026-06-04T15:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "No se encontró el producto con id 99"
}
```

| Situación             | Código HTTP |
|-----------------------|-------------|
| Recurso no encontrado | 404         |
| SKU duplicado         | 409         |
| Error de validación   | 400         |

## Estructura del proyecto

```
src/main/java/pe/com/pacifico/productos
├── config        Configuración (OpenAPI)
├── controller    Endpoints REST reactivos
├── domain        Entidad de persistencia
├── dto           Objetos de entrada/salida (records)
├── exception     Excepciones de negocio y handler global
├── mapper        Conversión entidad <-> DTO
├── repository    Repositorio reactivo R2DBC
└── service       Lógica de negocio
```