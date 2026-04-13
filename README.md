# Food Court Service Microservicio

Microservicio de gestion de plazoleta de comidas para restaurantes, platos, pedidos y reportes operativos.

## Documentacion consolidada

- Requisitos compartidos: `..\requirements.md`
- Coleccion Postman unica: `..\postman_collections\plazoleta-pragma.postman_collection.json`

## Estado funcional vs requirements

| HU  | Endpoint / Regla                               | Estado |
|-----|------------------------------------------------|--------|
| #2  | Crear restaurante (ADMIN)                      | OK     |
| #5  | Restriccion por rol para crear restaurante     | OK     |
| #3  | Crear plato                                    | X      |
| #4  | Actualizar plato                               | X      |
| #7  | Activar/desactivar plato                       | X      |
| #9  | Listar restaurantes                            | X      | 
| #10 | Listar menu por restaurante                    | X      |
| #11 | Crear pedido                                   | X      |
| #12 | Listar pedidos por estado                      | X      |
| #13 | Asignar pedido/cambiar estado                  | X      |
| #14 | Notificar pedido listo                         | X      |
| #15 | Entregar pedido con PIN                        | X      |
| #16 | Cancelar pedido                                | X      |
| #17 | Historial de cambios de estado                 | X      |
| #18 | Eficiencia de pedidos por restaurante/empleado | X      |

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
