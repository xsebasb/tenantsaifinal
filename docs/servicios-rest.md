# Documentación de servicios REST

Este documento describe los servicios REST expuestos por la aplicación `tenantmanager`. Las rutas están divididas en dos grupos principales:

* **Tenants API** (`/tenants-api`): expone operaciones internas para la creación, consulta y estadística de tenants y clientes.
* **Configuraciones compartidas** (`/api/shared/configurations`): actúa como _proxy_ hacia la API externa de Lirium para planes de folios y suscripciones.

Las URLs base consumidas desde este servicio se encuentran parametrizadas en `config.cfg`:

* `url.token`: endpoint para obtener el _access token_.
* `url.create-tenant`: endpoint remoto para crear tenants.
* `url.tenants-base`: endpoint remoto para operaciones de lectura sobre tenants.
* `url.tenants.base.tenants`: URL raíz usada por los controladores de folios para construir las rutas proxy.【F:config.cfg†L1-L8】

Salvo que se indique lo contrario, todas las respuestas exitosas utilizan JSON y los códigos HTTP estándar (`200 OK`, `202 Accepted`, etc.). Los errores imprevistos son devueltos con `500 Internal Server Error` junto con un objeto `{ "error": ..., "detail": ... }`.

## Autenticación

Los controladores obtienen automáticamente un token Bearer antes de llamar a la API externa usando `LiriumService.obtenerToken()`. No es necesario que el consumidor proporcione encabezados de autenticación adicionales al invocar estos endpoints internos.【F:src/main/java/com/gruposai/tenantmanager/Service/LiriumService.java†L25-L45】【F:src/main/java/com/gruposai/tenantmanager/Controller/TenantController.java†L41-L47】

---

## Tenants API (`/tenants-api`)

### POST `/tenants-api/crear`
Crea un nuevo tenant en la plataforma externa.

**Body (JSON)** — coincide con `TenantRequest`:
```json
{
  "document": "string",
  "verification_digit": 0,
  "company_name": "string",
  "company_email": "string",
  "whatsapp_number": "string",
  "company_address": "string",
  "company_phone": "string"
}
```

**Respuesta 202 (Accepted)** — `TenantCreationResponse`:
```json
{
  "message": "Tenant creado",
  "schema_name": "tenant_123",
  "company_id": "uuid",
  "company_name": "Empresa S.A."
}
```
En caso de error se devuelve `500` con detalles del fallo.【F:src/main/java/com/gruposai/tenantmanager/Controller/TenantController.java†L31-L38】【F:src/main/java/com/gruposai/tenantmanager/DTO/TenantRequest.java†L6-L14】【F:src/main/java/com/gruposai/tenantmanager/DTO/TenantCreationResponse.java†L5-L36】

### GET `/tenants-api/token`
Devuelve el token de acceso gestionado por el backend para diagnosticar problemas de autenticación.

**Respuesta 200**
```json
{ "access": "token.jwt" }
```
【F:src/main/java/com/gruposai/tenantmanager/Controller/TenantController.java†L41-L47】

### GET `/tenants-api/by-idn/{id_n}`
Consulta clientes locales por su identificador `id_n` utilizando la base de datos Firebird.

**Respuesta 200** — lista de `CustomerProjection`:
```json
[
  {
    "id_n": "123",
    "company": "Empresa S.A.",
    "addr1": "Calle 123",
    "phone1": "+57 3000000000",
    "phone2": null,
    "cv": "Nit",
    "email": "contacto@empresa.com"
  }
]
```
【F:src/main/java/com/gruposai/tenantmanager/Controller/TenantController.java†L51-L53】【F:src/main/java/com/gruposai/tenantmanager/DTO/CustomerProjection.java†L5-L11】

### GET `/tenants-api`
Lista tenants remotos con soporte de filtros opcionales (los parámetros recibidos se reenvían a la API externa).

**Parámetros de consulta** — los mismos que acepta la API de Lirium (p. ej. `page`, `search`, etc.).

**Respuesta 200** — `TenantListResponse`:
```json
{
  "count": 120,
  "next": "https://.../api/tenants/?page=3",
  "previous": "https://.../api/tenants/?page=1",
  "results": [
    {
      "id": 1,
      "schema_name": "tenant_001",
      "name": "Tenant 001",
      "paid_until": "2024-12-31",
      "on_trial": false,
      "created_on": "2024-01-10T12:00:00Z",
      "company_name": "Empresa 001",
      "company_document": "900000001",
      "is_active": true,
      "is_expired": false,
      "_str": "Tenant 001",
      "_status_display": "Activo"
    }
  ],
  "total_pages": 6,
  "current_page": 2,
  "page_size": 20
}
```
【F:src/main/java/com/gruposai/tenantmanager/Controller/TenantController.java†L58-L65】【F:src/main/java/com/gruposai/tenantmanager/DTO/TenantListResponse.java†L8-L69】【F:src/main/java/com/gruposai/tenantmanager/DTO/TenantListItem.java†L7-L21】

### GET `/tenants-api/lookup`
Devuelve resultados simplificados para cuadros de búsqueda incremental.

**Respuesta 200** — lista de `TenantLookupItem` con las claves `value`, `label`, `schema_name` y `name`.
【F:src/main/java/com/gruposai/tenantmanager/Controller/TenantController.java†L68-L75】【F:src/main/java/com/gruposai/tenantmanager/DTO/TenantLookupItem.java†L5-L9】

### GET `/tenants-api/search_autocomplete`
Realiza búsquedas autocompletadas de tenants y devuelve metadatos básicos.

**Respuesta 200** — lista de `TenantAutocompleteItem`.
Campos principales: `id`, `schema_name`, `name`, `paid_until`, `on_trial`, `company`, `company_name`, `company_document`.
【F:src/main/java/com/gruposai/tenantmanager/Controller/TenantController.java†L78-L85】【F:src/main/java/com/gruposai/tenantmanager/DTO/TenantAutocompleteItem.java†L7-L14】

### GET `/tenants-api/{id}`
Obtiene el detalle bruto (`JsonNode`) de un tenant específico. Se devuelve la estructura recibida de la API externa.
【F:src/main/java/com/gruposai/tenantmanager/Controller/TenantController.java†L88-L95】

### GET `/tenants-api/{id}/summary`
Devuelve un resumen estructurado (`TenantSummary`) del tenant, que incluye información de compañía, estado de prueba y métricas de uso.
Campos destacados: `company_info`, `trial_status`, `usage_summary`, `days_until_expiration`.
【F:src/main/java/com/gruposai/tenantmanager/Controller/TenantController.java†L98-L104】【F:src/main/java/com/gruposai/tenantmanager/DTO/TenantSummary.java†L7-L40】

### GET `/tenants-api/stats`
Retorna estadísticas agregadas de tenants (`TenantStats`): `total`, `active`, `on_trial`, `expired`, `expiring_soon`.
【F:src/main/java/com/gruposai/tenantmanager/Controller/TenantController.java†L107-L113】【F:src/main/java/com/gruposai/tenantmanager/DTO/TenantStats.java†L5-L10】

---

## Configuraciones compartidas (`/api/shared/configurations`)
Estos endpoints funcionan como proxy hacia la API externa utilizando la URL base configurada en `LiriumConfig`. Todos requieren token Bearer, el cual es gestionado automáticamente.【F:src/main/java/com/gruposai/tenantmanager/Controller/FolioController.java†L33-L55】【F:src/main/java/com/gruposai/tenantmanager/Util/LiriumConfig.java†L11-L93】

### GET `/api/shared/configurations/folio-plans`
Obtiene los planes de folios disponibles. Acepta opcionalmente `page` para paginación.

**Respuesta 200** — `FolioPlanResponse` con los campos de paginación estándar y un arreglo `results` de `FolioPlan` (incluye `id`, `name`, `description`, `price`, `folios_included`, `distributor_name`, `active`, `created`, `updated`).
【F:src/main/java/com/gruposai/tenantmanager/Controller/FolioController.java†L45-L91】【F:src/main/java/com/gruposai/tenantmanager/DTO/FolioPlanResponse.java†L7-L61】【F:src/main/java/com/gruposai/tenantmanager/DTO/FolioPlan.java†L8-L74】

### POST `/api/shared/configurations/tenant-folio-subscriptions`
Crea una suscripción de folios para un tenant.

**Body (JSON)** — `TenantFolioSubscription` (campos relevantes: `tenant`, `plan`, `folios_purchased`, `purchase_date`, `auto_renew`, etc.).

**Respuesta 200** — el objeto `TenantFolioSubscription` resultante con detalles del plan y tenant asociados.
【F:src/main/java/com/gruposai/tenantmanager/Controller/FolioController.java†L93-L123】【F:src/main/java/com/gruposai/tenantmanager/DTO/TenantFolioSubscription.java†L7-L118】

### GET `/api/shared/configurations/tenant-folio-subscriptions`
Lista suscripciones de folios. Acepta el parámetro opcional `tenant` para filtrar por identificador de tenant.

**Respuesta 200** — arreglo de `TenantFolioSubscription`.
【F:src/main/java/com/gruposai/tenantmanager/Controller/FolioController.java†L127-L153】

### GET `/api/shared/configurations/tenant-folio-subscriptions-paged`
Versión paginada del listado de suscripciones. Acepta `tenant`, `page` y `page_size`.

**Respuesta 200** — `TenantFolioSubscriptionResponse` con campos de paginación (`count`, `current_page`, `total_pages`, etc.) y `results`.
【F:src/main/java/com/gruposai/tenantmanager/Controller/FolioController.java†L166-L203】【F:src/main/java/com/gruposai/tenantmanager/DTO/TenantFolioSubscriptionResponse.java†L8-L45】

---

## Manejo de errores

* Los endpoints de Tenants retornan un objeto `{ "error": "mensaje", "detail": "descripcion" }` en caso de excepciones internas.【F:src/main/java/com/gruposai/tenantmanager/Controller/TenantController.java†L116-L121】
* Los endpoints de folios registran los errores y devuelven códigos HTTP provenientes de la API remota cuando están disponibles. Si ocurre una excepción inesperada se responde con `500` y cuerpos vacíos u objetos inicializados.【F:src/main/java/com/gruposai/tenantmanager/Controller/FolioController.java†L84-L90】【F:src/main/java/com/gruposai/tenantmanager/Controller/FolioController.java†L111-L123】【F:src/main/java/com/gruposai/tenantmanager/Controller/FolioController.java†L193-L202】

## Consideraciones adicionales

* Los controladores aceptan y devuelven JSON codificado en UTF-8.
* Las clases DTO están anotadas para ignorar campos desconocidos, permitiendo compatibilidad hacia adelante cuando la API de Lirium agregue propiedades nuevas.
* Los parámetros de consulta enviados a `/tenants-api` se reenvían directamente, por lo que es posible aprovechar todos los filtros disponibles en la API externa sin cambios en el backend.
