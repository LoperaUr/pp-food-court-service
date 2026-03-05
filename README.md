# Food Court Service Microservicio

Microservicio de gestión de plazoleta de comidas que maneja restaurantes, platos, pedidos y empleados.

---

## 📋 Status Check - Implementación de Endpoints

| # | Endpoint | Método | Rol Requerido | Estado | HU |
|---|----------|--------|---------------|--------|-----|
| 1 | `/restaurants` | POST | Administrador | ❌ No Implementado | #2 |
| 2 | `/restaurants` | GET | Cliente | ❌ No Implementado | #9 |
| 3 | `/restaurants/{id}/dishes` | POST | Propietario | ❌ No Implementado | #3 |
| 4 | `/restaurants/{id}/dishes/{dishId}` | PUT | Propietario | ❌ No Implementado | #4 |
| 5 | `/restaurants/{id}/dishes/{dishId}/activate` | PATCH | Propietario | ❌ No Implementado | #7 |
| 6 | `/restaurants/{id}/dishes` | GET | Cliente | ❌ No Implementado | #10 |
| 7 | `/orders` | POST | Cliente | ❌ No Implementado | #11 |
| 8 | `/orders/{orderId}` | DELETE | Cliente | ❌ No Implementado | #16 |
| 9 | `/orders/{orderId}/history` | GET | Cliente | ❌ No Implementado | #17 |
| 10 | `/restaurants/{id}/orders` | GET | Empleado | ❌ No Implementado | #12 |
| 11 | `/orders/{orderId}/assign` | PATCH | Empleado | ❌ No Implementado | #13 |
| 12 | `/orders/{orderId}/status` | PATCH | Empleado | ❌ No Implementado | #13 |
| 13 | `/orders/{orderId}/ready` | PATCH | Empleado | ❌ No Implementado | #14 |
| 14 | `/orders/{orderId}/deliver` | PATCH | Empleado | ❌ No Implementado | #15 |
| 15 | `/restaurants/{id}/efficiency` | GET | Propietario | ❌ No Implementado | #18 |

---

## 🎯 Descripción de Endpoints

### 🏪 Gestión de Restaurantes

#### 1. Crear Restaurante (Admin)
```
POST /restaurants
```
- **Rol Requerido:** Administrador
- **Campos Requeridos:** Nombre, NIT, Dirección, Teléfono, UrlLogo, idUsuarioPropietario
- **Validaciones:** 
  - NIT y teléfono numéricos
  - Teléfono máx 13 caracteres (puede incluir +)
  - Nombre no puede ser solo números
  - Usuario propietario debe existir con rol correcto

#### 2. Listar Restaurantes (Cliente)
```
GET /restaurants?pageNumber=1&pageSize=10
```
- **Rol Requerido:** Cliente
- **Parámetros:** pageNumber, pageSize
- **Respuesta:** Nombre, UrlLogo (ordenados alfabéticamente)

---

### 🍽️ Gestión de Platos

#### 3. Crear Plato (Propietario)
```
POST /restaurants/{restaurantId}/dishes
```
- **Rol Requerido:** Propietario del restaurante
- **Campos Requeridos:** Nombre, Precio, Descripción, UrlImagen, Categoría
- **Validaciones:** Precio entero > 0, solo propietario del restaurante
- **Estado Inicial:** Activo (true)

#### 4. Actualizar Plato (Propietario)
```
PUT /restaurants/{restaurantId}/dishes/{dishId}
```
- **Rol Requerido:** Propietario del restaurante
- **Campos Modificables:** Precio, Descripción
- **Validaciones:** Solo propietario del restaurante, no modificar otros restaurantes

#### 5. Activar/Desactivar Plato (Propietario)
```
PATCH /restaurants/{restaurantId}/dishes/{dishId}/activate
```
- **Rol Requerido:** Propietario del restaurante
- **Acción:** Cambiar estado activo/inactivo
- **Validaciones:** Solo propietario del restaurante

#### 6. Listar Platos de Restaurante (Cliente)
```
GET /restaurants/{restaurantId}/dishes?pageNumber=1&pageSize=10&category=CATEGORIA
```
- **Rol Requerido:** Cliente
- **Parámetros:** pageNumber, pageSize, category (opcional)
- **Respuesta:** Lista paginada de platos

---

### 📦 Gestión de Pedidos - Cliente

#### 7. Crear Pedido (Cliente)
```
POST /orders
```
- **Rol Requerido:** Cliente
- **Campos Requeridos:** idRestaurante, lista de {idPlato, cantidad}
- **Validaciones:**
  - Todos los platos del mismo restaurante
  - Cliente sin pedidos en proceso
  - Estado inicial: "Pendiente"

#### 8. Cancelar Pedido (Cliente)
```
DELETE /orders/{orderId}
```
- **Rol Requerido:** Cliente (propietario del pedido)
- **Validaciones:** Solo pedidos en estado "Pendiente"

#### 9. Obtener Historial de Pedido (Cliente)
```
GET /orders/{orderId}/history
```
- **Rol Requerido:** Cliente (propietario del pedido)
- **Respuesta:** Historial de cambios de estado

---

### 👨‍🍳 Gestión de Pedidos - Empleado

#### 10. Listar Pedidos del Restaurante (Empleado)
```
GET /restaurants/{restaurantId}/orders?status=PENDIENTE&pageNumber=1&pageSize=10
```
- **Rol Requerido:** Empleado del restaurante
- **Parámetros:** status, pageNumber, pageSize
- **Respuesta:** Todos los campos del pedido

#### 11. Asignarse a un Pedido (Empleado)
```
PATCH /orders/{orderId}/assign
```
- **Rol Requerido:** Empleado
- **Acción:** Asignar ID del empleado al campo EmpleadoAsignado

#### 12. Cambiar Estado de Pedido (Empleado)
```
PATCH /orders/{orderId}/status
```
- **Rol Requerido:** Empleado del restaurante
- **Estados Válidos:** pendiente → en_preparacion → listo → entregado
- **Validaciones:** Cambios de estado secuenciales

#### 13. Marcar como Listo (Empleado)
```
PATCH /orders/{orderId}/ready
```
- **Rol Requerido:** Empleado del restaurante
- **Acción:** Cambiar a "Listo" y generar PIN
- **Efecto:** Enviar SMS al cliente con PIN

#### 14. Marcar como Entregado (Empleado)
```
PATCH /orders/{orderId}/deliver
```
- **Rol Requerido:** Empleado del restaurante
- **Parámetros:** PIN de seguridad
- **Validaciones:** 
  - Pedido en estado "Listo"
  - PIN correcto
  - No se puede modificar después de entregar

---

### 📊 Reportes

#### 15. Obtener Eficiencia del Restaurante (Propietario)
```
GET /restaurants/{restaurantId}/efficiency
```
- **Rol Requerido:** Propietario del restaurante
- **Respuesta:**
  - Tiempo entre inicio y fin de cada pedido
  - Ranking de empleados por tiempo promedio

---

## 🚀 Guía para construir y desplegar la imagen Docker en AWS ECR

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

