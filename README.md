# Food Court Service Microservicio

Microservicio de gestion de plazoleta de comidas para restaurantes, platos, pedidos y reportes operativos.

## Documentacion consolidada

- Requisitos compartidos: `..\requirements.md`
- Coleccion Postman unica: `..\postman_collections\plazoleta-pragma.postman_collection.json`

## Estado funcional vs requirements

| HU | Endpoint / Regla | Estado | Evidencia en el repo |
|---|---|---|---|
| #2 | Crear restaurante (ADMIN) | Implementada | `POST /restaurants` en `RestaurantController` |
| #5 | Restriccion por rol para crear restaurante | Parcial | `@PreAuthorize("hasRole('ADMIN')")` en `RestaurantController` |
| #3 | Crear plato | Pendiente | Sin controlador/endpoint expuesto |
| #4 | Actualizar plato | Pendiente | Sin controlador/endpoint expuesto |
| #7 | Activar/desactivar plato | Pendiente | Sin controlador/endpoint expuesto |
| #9 | Listar restaurantes | Pendiente | Sin endpoint `GET /restaurants` |
| #10 | Listar menu por restaurante | Pendiente | Sin endpoint expuesto |
| #11 | Crear pedido | Pendiente | Sin endpoint expuesto |
| #12 | Listar pedidos por estado | Pendiente | Sin endpoint expuesto |
| #13 | Asignar pedido/cambiar estado | Pendiente | Sin endpoint expuesto |
| #14 | Notificar pedido listo | Pendiente | Sin endpoint expuesto |
| #15 | Entregar pedido con PIN | Pendiente | Sin endpoint expuesto |
| #16 | Cancelar pedido | Pendiente | Sin endpoint expuesto |
| #17 | Historial de cambios de estado | Pendiente | Sin endpoint expuesto |
| #18 | Eficiencia de pedidos por restaurante/empleado | Pendiente | Sin endpoint expuesto |

## Status de endpoints

| Endpoint | Metodo | Rol requerido | Estado actual |
|---|---|---|---|
| `/restaurants` | POST | Administrador | Implementado |
| `/restaurants` | GET | Cliente | Pendiente |
| `/restaurants/{restaurantId}/dishes` | POST | Propietario | Pendiente |
| `/restaurants/{restaurantId}/dishes/{dishId}` | PUT | Propietario | Pendiente |
| `/restaurants/{restaurantId}/dishes/{dishId}/activate` | PATCH | Propietario | Pendiente |
| `/restaurants/{restaurantId}/dishes` | GET | Cliente | Pendiente |
| `/orders` | POST | Cliente | Pendiente |
| `/orders/{orderId}` | DELETE | Cliente | Pendiente |
| `/orders/{orderId}/history` | GET | Cliente | Pendiente |
| `/restaurants/{restaurantId}/orders` | GET | Empleado | Pendiente |
| `/orders/{orderId}/assign` | PATCH | Empleado | Pendiente |
| `/orders/{orderId}/status` | PATCH | Empleado | Pendiente |
| `/orders/{orderId}/ready` | PATCH | Empleado | Pendiente |
| `/orders/{orderId}/deliver` | PATCH | Empleado | Pendiente |
| `/restaurants/{restaurantId}/efficiency` | GET | Propietario | Pendiente |

## Guia para construir y desplegar la imagen Docker en AWS ECR

### 1. Construir la imagen Docker

```sh
docker build -t nombre-imagen:tag .
```

### 2. Crear un repositorio en ECR (si no existe)

```sh
aws ecr create-repository --repository-name nombre-repositorio
```

### 3. Autenticarse en ECR

```sh
aws ecr get-login-password --region tu-region | docker login --username AWS --password-stdin <tu-id-cuenta>.dkr.ecr.<tu-region>.amazonaws.com
```

### 4. Etiquetar la imagen

```sh
docker tag nombre-imagen:tag <tu-id-cuenta>.dkr.ecr.<tu-region>.amazonaws.com/nombre-repositorio:tag
```

### 5. Subir la imagen a ECR

```sh
docker push <tu-id-cuenta>.dkr.ecr.<tu-region>.amazonaws.com/nombre-repositorio:tag
```

> Reemplaza `nombre-imagen`, `tag`, `nombre-repositorio`, `tu-region` y `tu-id-cuenta` por los valores correspondientes.
