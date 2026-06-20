# TP5 — Gestión de Inventario Inteligente
## Microservicio REST con Arquitectura en Capas y Patrones de Diseño

Este proyecto corresponde al Trabajo Práctico Integrador N.º 5 de la materia **Programación III** de la carrera **Ingeniería en Sistemas de Información** de la **Universidad Nacional de La Rioja (UNLaR)**, desarrollado en el ciclo lectivo 2026.

El sistema implementa un microservicio REST para la gestión integral del inventario de un depósito inteligente. Permite administrar productos y categorías, registrar de manera segura y concurrente movimientos de stock (entradas y salidas), disparar alertas automatizadas parametrizables por configuración mediante patrones de diseño, y auditar la performance algorítmica del microservicio frente a cargas masivas de datos.

---

## 2. Tecnologías y Stack Utilizado

| Componente | Tecnología / Versión |
|---|---|
| **Lenguaje** | Java 21 (Long Term Support) |
| **Framework principal** | Spring Boot 3.5.15 |
| **Gestor de dependencias** | Maven 3.9.x (con Wrapper incorporado) |
| **Documentación de API** | Springdoc-OpenAPI / Swagger UI 2.8.16 |
| **Herramientas auxiliares** | Lombok y Jakarta Bean Validation |
| **Almacenamiento** | In-Memory Thread-Safe Storage (ConcurrentHashMap + AtomicLong) |

---

## 3. Arquitectura del Proyecto y Principios Aplicados

El proyecto está diseñado bajo una arquitectura en capas estrictamente desacoplada mediante inyección de dependencias por constructor (respetando la regla de **cero `@Autowired` en atributos**), garantizando alta cohesión y bajo acoplamiento:

1. **Capa del Modelo (Domain Model):** Contiene las entidades puras del negocio (`Producto`, `Categoria`, `MovimientoInventario`). Aplica **composición sobre herencia** (un `Producto` *tiene una* `Categoria`).
2. **Capa de Repositorio (Persistence Layer):** Diseñada de forma genérica con la interfaz `IGenericRepository<T, ID>` y la clase abstracta `GenericInMemoryRepository<T, ID>`. Emplea un `ConcurrentHashMap` para el almacenamiento de colecciones de datos y un `AtomicLong` para la generación libre de bloqueos de identificadores secuenciales.
3. **Capa de Servicio (Business Logic Layer):** Centraliza las reglas de negocio. Utiliza `AtomicInteger` para alterar el stock del producto de forma atómica, previniendo condiciones de carrera y problemas de concurrencia entre hilos.
4. **Capa del Controlador (REST Web API Layer):** Expone los endpoints HTTP. Recibe solicitudes, valida la integridad de los datos de entrada mediante la anotación `@Valid` aplicada sobre Records DTO inmutables, y delega la ejecución de la lógica en la capa de servicios.

### Patrones de Diseño e Integración SOLID

* **Single Responsibility Principle (SRP):** Cada clase tiene un único motivo de cambio. Las operaciones REST, las conversiones DTO y la lógica transaccional están completamente aisladas en sus estratos correspondientes.
* **Open/Closed Principle (OCP) & Patrón Strategy:** La evaluación de alertas de stock bajo e inventario crítico se delega a la interfaz `ReglaAlertaStrategy`. La lógica se lee en tiempo de ejecución desde el archivo de configuración a través del componente `UmbralesConfigurablesStrategy`, permitiendo añadir nuevos algoritmos de análisis sin modificar el código existente.
* **Liskov Substitution Principle (LSP):** Los repositorios específicos extienden correctamente las abstracciones genéricas sin alterar el comportamiento de los contratos base.
* **Interface Segregation Principle (ISP):** Las interfaces de repositorios y servicios están particionadas y especializadas por entidad.
* **Dependency Inversion Principle (DIP):** Los componentes de software dependen de abstracciones (interfaces), inyectadas a través de constructores administrados por el contenedor IoC de Spring Boot.

---

## 4. Estructura de Paquetes

```
ar.edu.unlar.tp5_inventario/
├── Tp5InventarioApplication.java           # Clase principal con activación de propiedades
├── config/
│   ├── DataInitializer.java                # Carga de datos iniciales (Bootstrap)
│   └── StockConfig.java                    # Mapeo inmutable de umbrales YAML/Properties
├── controller/
│   ├── AdminController.java                # Endpoint para el Performance Report administrativo
│   ├── AlertaController.java               # Endpoint de consulta de alertas de stock bajo
│   ├── CategoriaController.java            # CRUD completo de categorías
│   ├── MovimientoController.java           # Registros transaccionales de stock e historial
│   └── ProductoController.java             # Catálogo, búsquedas avanzadas y ordenamiento
├── dto/
│   ├── AlertaStockResponse.java            # DTO para listado de productos en alerta
│   ├── CategoriaRequest.java               # Validación de entradas para categorías
│   ├── CategoriaResponse.java              # Estructura de salida para categorías
│   ├── MetricaPerformance.java             # Formato estructurado del reporte Big O
│   ├── MovimientoRequest.java              # Solicitud de entrada/salida de mercadería
│   ├── MovimientoResponse.java             # Registro de auditoría del movimiento
│   ├── ProductoRequest.java                # Validación estricta con Jakarta Bean Validation
│   └── ProductoResponse.java               # Estructura de salida inmutable para productos
├── exception/
│   ├── BusinessRuleException.java          # Error para restricciones lógicas (HTTP 409)
│   ├── GlobalExceptionHandler.java         # Interceptor ControllerAdvice centralizado
│   ├── InsufficientStockException.java     # Error para quiebres de stock (HTTP 409)
│   └── ResourceNotFoundException.java      # Error para IDs inexistentes (HTTP 404)
├── model/
│   ├── Categoria.java                      # Entidad básica de categoría
│   ├── MovimientoInventario.java           # Registro cronológico del inventario
│   ├── NivelAlerta.java                    # Enum: NORMAL, BAJO, CRITICO
│   ├── Producto.java                       # Entidad con control de stock mediante AtomicInteger
│   └── TipoMovimiento.java                 # Enum: ENTRADA, SALIDA
├── repository/
│   ├── CategoriaRepository.java
│   ├── GenericInMemoryRepository.java      # Base abstracta concurrente
│   ├── IGenericRepository.java             # Interfaz CRUD parametrizada
│   ├── InMemoryCategoriaRepository.java
│   ├── InMemoryMovimientoRepository.java
│   ├── InMemoryProductoRepository.java     # Consultas con Streams avanzados O(n)
│   ├── MovimientoRepository.java
│   └── ProductoRepository.java
└── service/
    ├── AlertaService.java
    ├── AlertaServiceImpl.java
    ├── CategoriaService.java
    ├── CategoriaServiceImpl.java           # Lógica e integridad en borrado de categorías
    ├── MovimientoService.java
    ├── MovimientoServiceImpl.java          # Lógica thread-safe para control atómico de stock
    ├── PerformanceReportService.java       # Motor de benchmarks masivos (1k a 100k elementos)
    ├── ProductoService.java
    ├── ProductoServiceImpl.java            # Filtros dinámicos y ordenamiento TimSort O(n log n)
    ├── ReglaAlertaStrategy.java            # Abstracción del patrón Strategy
    └── UmbralesConfigurablesStrategy.java
```

---

## 5. Endpoints de la API REST

### Categorías

| Método | Endpoint | Descripción |
|---|---|---|
| `GET` | `/api/categorias` | Lista todas las categorías |
| `GET` | `/api/categorias/{id}` | Obtiene los detalles de una categoría por su ID |
| `POST` | `/api/categorias` | Crea una nueva categoría (body: `CategoriaRequest`) |
| `PUT` | `/api/categorias/{id}` | Actualiza una categoría existente |
| `DELETE` | `/api/categorias/{id}` | Elimina una categoría (`409 Conflict` si contiene productos) |

### Productos

| Método | Endpoint | Descripción |
|---|---|---|
| `GET` | `/api/productos` | Catálogo completo. Soporta query params: `categoriaId`, `precioMin`, `precioMax`, `enStock` |
| `GET` | `/api/productos/{id}` | Obtiene los detalles de un producto por su ID |
| `POST` | `/api/productos` | Crea un producto vinculándolo a una categoría (body: `ProductoRequest`) |
| `PUT` | `/api/productos/{id}` | Modifica un producto (el stock se omite en esta operación) |
| `DELETE` | `/api/productos/{id}` | Elimina físicamente un producto |
| `GET` | `/api/productos/buscar?q=` | Búsqueda textual case-insensitive por nombre |
| `GET` | `/api/productos/ordenados?campo=&orden=` | Lista productos ordenados por `nombre`, `precio` o `stock` en orden `asc` / `desc` |

### Movimientos de Inventario

| Método | Endpoint | Descripción |
|---|---|---|
| `POST` | `/api/movimientos` | Registra una `ENTRADA` o `SALIDA`. Si la cantidad supera el stock disponible, responde `409 Conflict` |
| `GET` | `/api/movimientos/producto/{id}` | Devuelve el historial cronológico completo de auditoría para un producto |

### Alertas y Administración

| Método | Endpoint | Descripción |
|---|---|---|
| `GET` | `/api/alertas/stock-bajo` | Lista en tiempo real los productos en estado `BAJO` o `CRITICO` (patrón Strategy) |
| `GET` | `/api/admin/performance-report` | Ejecuta benchmarks con 1 000, 10 000 y 100 000 elementos para validar la complejidad Big O de los algoritmos |

---

## 6. Configuración e Instalación del Entorno

### Prerrequisitos

* **Java Development Kit (JDK):** Versión 21 o superior, configurada en las variables de entorno (`JAVA_HOME`).
* **Apache Maven:** Versión 3.9+ (opcional; puede usarse el script `mvnw` incluido en la raíz).

### Instrucciones de Ejecución

**1. Clonar el repositorio o situarse en la raíz del proyecto:**
```bash
cd tp5-inventario
```

**2. Limpiar e instalar dependencias completando el ciclo de vida de Maven:**
```bash
./mvnw clean package
```

**3. Iniciar la aplicación Spring Boot:**
```bash
./mvnw spring-boot:run
```

**4. Acceso a las herramientas web:**

| Recurso | URL |
|---|---|
| Servidor REST | `http://localhost:8080` |
| Swagger UI (documentación interactiva) | http://localhost:8080/swagger-ui.html |
| OpenAPI JSON (spec cruda) | http://localhost:8080/api-docs |

### Configuración de `application.properties`

Los umbrales de alerta son completamente dinámicos y pueden redefinirse modificando las siguientes propiedades:

```properties
# Umbrales del depósito inteligente para el patrón Strategy
inventario.stock.minimo=10
inventario.stock.critico=3
```

---

## 7. Generación de JavaDoc

El código fuente cuenta con cobertura del 100% en clases públicas, interfaces y firmas de métodos públicos con comentarios estructurados listos para compilar. Para generar la suite de páginas HTML:

```bash
./mvnw javadoc:javadoc
```

Los artefactos generados se guardarán en `target/site/apidocs/index.html`.

---

## 8. Autores / Integrantes del Grupo

* **Francisco Antonio González**
* **Virginia Vera**
* **Ismael Flores**


---

