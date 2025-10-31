package com.gruposai.tenantmanager.Controller;

import com.gruposai.tenantmanager.DTO.FolioPlan;
import com.gruposai.tenantmanager.DTO.FolioPlanResponse;
import com.gruposai.tenantmanager.DTO.TenantFolioSubscription;
import com.gruposai.tenantmanager.DTO.TenantFolioSubscriptionResponse;
import com.gruposai.tenantmanager.Service.LiriumService;
import com.gruposai.tenantmanager.Util.LiriumConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;
import java.util.Collections;
import java.util.Arrays;
import java.util.Iterator;

@RestController
@RequestMapping("/api/shared/configurations")
public class FolioController {

    private static final Logger log = LoggerFactory.getLogger(FolioController.class);

    @Autowired
    private LiriumService liriumService;

    @Autowired
    private LiriumConfig config;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    // Soportar ambas rutas con y sin barra final
    @GetMapping({"/folio-plans", "/folio-plans/"})
    public ResponseEntity<FolioPlanResponse> obtenerPlanesDefolios(@RequestParam(value = "page", required = false) Integer page) {
        String url = null;
        try {
            String token = liriumService.obtenerToken();
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<Void> request = new HttpEntity<>(headers);
            String baseUrl = getBaseUrl();
            url = baseUrl + "/api/shared/configurations/folio-plans";
            if (page != null) {
                url += "?page=" + page;
            }
            log.info("[FOLIO-PLANS] Enviando petición GET a {}", url);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    request,
                    String.class
            );
            log.info("[FOLIO-PLANS] Status recibido: {}", response.getStatusCodeValue());
            String body = response.getBody();
            if (body == null) {
                log.warn("[FOLIO-PLANS] Body nulo");
                return ResponseEntity.ok(new FolioPlanResponse());
            }
            if (log.isDebugEnabled()) {
                log.debug("[FOLIO-PLANS] Body crudo (primeros 500 chars): {}", body.substring(0, Math.min(500, body.length())));
            }
            if (!response.getStatusCode().is2xxSuccessful()) {
                log.warn("[FOLIO-PLANS] Respuesta no exitosa {}", response.getStatusCode());
                return ResponseEntity.status(response.getStatusCode()).body(new FolioPlanResponse());
            }

            FolioPlanResponse folioResponse = parseFolioPlansResponse(body);
            log.info("[FOLIO-PLANS] Cantidad de planes parseados: {}", folioResponse.getResults() != null ? folioResponse.getResults().size() : 0);
            return ResponseEntity.ok(folioResponse);
        } catch (RestClientResponseException re) {
            log.error("[FOLIO-PLANS] Error HTTP al llamar {} Status={} Body={}", url, re.getRawStatusCode(), re.getResponseBodyAsString());
            return ResponseEntity.status(re.getRawStatusCode()).body(new FolioPlanResponse());
        } catch (Exception e) {
            log.error("[FOLIO-PLANS] Error obteniendo planes de folios en {}: {}", url, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new FolioPlanResponse());
        }
    }

    @PostMapping({"/tenant-folio-subscriptions", "/tenant-folio-subscriptions/"})
    public ResponseEntity<TenantFolioSubscription> crearSuscripcionFolio(@RequestBody TenantFolioSubscription suscripcion) {
        String url = null;
        try {
            String token = liriumService.obtenerToken();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(token);
            HttpEntity<TenantFolioSubscription> request = new HttpEntity<>(suscripcion, headers);
            String baseUrl = getBaseUrl();
            url = baseUrl + "/api/shared/configurations/tenant-folio-subscriptions/";
            log.info("[FOLIO-SUBSCRIPTIONS][POST] URL {}", url);
            ResponseEntity<TenantFolioSubscription> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    request,
                    TenantFolioSubscription.class
            );
            log.info("[FOLIO-SUBSCRIPTIONS][POST] Status {}", response.getStatusCodeValue());
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return ResponseEntity.ok(response.getBody());
            } else {
                log.warn("[FOLIO-SUBSCRIPTIONS][POST] Respuesta no exitosa {}", response.getStatusCode());
                return ResponseEntity.status(response.getStatusCode()).build();
            }
        } catch (RestClientResponseException re) {
            log.error("[FOLIO-SUBSCRIPTIONS][POST] Error HTTP {} status={} body={}", url, re.getRawStatusCode(), re.getResponseBodyAsString());
            return ResponseEntity.status(re.getRawStatusCode()).build();
        } catch (Exception e) {
            log.error("[FOLIO-SUBSCRIPTIONS][POST] Error creando suscripción en {}: {}", url, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping({"/tenant-folio-subscriptions", "/tenant-folio-subscriptions/"})
    public ResponseEntity<List<TenantFolioSubscription>> obtenerSuscripcionesFolios(
            @RequestParam(required = false) Integer tenant) {
        String url = null;
        try {
            String token = liriumService.obtenerToken();
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<Void> request = new HttpEntity<>(headers);
            String baseUrl = getBaseUrl();
            url = baseUrl + "/api/shared/configurations/tenant-folio-subscriptions";
            if (tenant != null) {
                url += "?tenant=" + tenant;
            }
            log.info("[FOLIO-SUBSCRIPTIONS][GET] URL {}", url);
            ResponseEntity<TenantFolioSubscription[]> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    request,
                    TenantFolioSubscription[].class
            );
            log.info("[FOLIO-SUBSCRIPTIONS][GET] Status {}", response.getStatusCodeValue());
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List<TenantFolioSubscription> lista = Arrays.asList(response.getBody());
                log.info("[FOLIO-SUBSCRIPTIONS][GET] Cantidad {}", lista.size());
                return ResponseEntity.ok(lista);
            } else {
                log.warn("[FOLIO-SUBSCRIPTIONS][GET] Respuesta no exitosa {}", response.getStatusCode());
                return ResponseEntity.status(response.getStatusCode()).body(Collections.emptyList());
            }
        } catch (RestClientResponseException re) {
            log.error("[FOLIO-SUBSCRIPTIONS][GET] Error HTTP {} status={} body={}", url, re.getRawStatusCode(), re.getResponseBodyAsString());
            return ResponseEntity.status(re.getRawStatusCode()).body(Collections.emptyList());
        } catch (Exception e) {
            log.error("[FOLIO-SUBSCRIPTIONS][GET] Error obteniendo suscripciones en {}: {}", url, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

    @GetMapping({"/tenant-folio-subscriptions-paged", "/tenant-folio-subscriptions-paged/"})
    public ResponseEntity<TenantFolioSubscriptionResponse> obtenerSuscripcionesFolios(
            @RequestParam(value = "tenant", required = false) Integer tenant,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "page_size", required = false) Integer pageSize) {
        String url = null;
        try {
            String token = liriumService.obtenerToken();
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<Void> request = new HttpEntity<>(headers);
            String baseUrl = getBaseUrl();
            StringBuilder sb = new StringBuilder(baseUrl).append("/api/shared/configurations/tenant-folio-subscriptions/?");
            if (tenant != null) sb.append("tenant=").append(tenant).append('&');
            if (page != null) sb.append("page=").append(page).append('&');
            if (pageSize != null) sb.append("page_size=").append(pageSize).append('&');
            url = sb.toString();
            if (url.endsWith("&") || url.endsWith("?")) url = url.substring(0, url.length() - 1);
            log.info("[FOLIO-SUBSCRIPTIONS-PAGED][GET] URL {}", url);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
            log.info("[FOLIO-SUBSCRIPTIONS-PAGED][GET] Status {}", response.getStatusCodeValue());
            String body = response.getBody();
            if (body == null) {
                return ResponseEntity.ok(new TenantFolioSubscriptionResponse());
            }
            if (!response.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.status(response.getStatusCode()).body(new TenantFolioSubscriptionResponse());
            }
            TenantFolioSubscriptionResponse parsed = parseSubscriptionResponse(body);
            return ResponseEntity.ok(parsed);
        } catch (RestClientResponseException re) {
            log.error("[FOLIO-SUBSCRIPTIONS-PAGED][GET] Error HTTP {} status={} body={}", url, re.getRawStatusCode(), re.getResponseBodyAsString());
            return ResponseEntity.status(re.getRawStatusCode()).body(new TenantFolioSubscriptionResponse());
        } catch (Exception e) {
            log.error("[FOLIO-SUBSCRIPTIONS-PAGED][GET] Error obteniendo suscripciones en {}: {}", url, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new TenantFolioSubscriptionResponse());
        }
    }

    private FolioPlanResponse parseFolioPlansResponse(String body) throws Exception {
        JsonNode root = mapper.readTree(body);

        // Si es directamente la estructura de respuesta paginada
        if (root.isObject() && root.has("results")) {
            log.info("[FOLIO-PLANS] Parseando respuesta paginada");
            return mapper.readValue(body, FolioPlanResponse.class);
        }

        // Si es un array directo (retrocompatibilidad)
        if (root.isArray()) {
            log.info("[FOLIO-PLANS] Parseando array directo de planes");
            List<FolioPlan> planes = mapper.readValue(body, new TypeReference<List<FolioPlan>>() {});
            FolioPlanResponse response = new FolioPlanResponse();
            response.setResults(planes);
            response.setCount(planes.size());
            response.setCurrentPage(1);
            response.setTotalPages(1);
            response.setPageSize(planes.size());
            response.setHasNext(false);
            response.setHasPrevious(false);
            return response;
        }

        // Si es un objeto sin structure de paginación
        if (root.isObject()) {
            // Buscar campos candidatos que sean arrays
            String[] candidates = {"data", "items", "folio_plans", "plans"};
            for (String c : candidates) {
                JsonNode node = root.get(c);
                if (node != null && node.isArray()) {
                    log.info("[FOLIO-PLANS] Usando campo '{}' para extraer planes (size={})", c, node.size());
                    List<FolioPlan> planes = mapper.readValue(node.toString(), new TypeReference<List<FolioPlan>>() {});
                    FolioPlanResponse response = new FolioPlanResponse();
                    response.setResults(planes);
                    response.setCount(planes.size());
                    response.setCurrentPage(1);
                    response.setTotalPages(1);
                    response.setPageSize(planes.size());
                    response.setHasNext(false);
                    response.setHasPrevious(false);
                    return response;
                }
            }

            // Si todo falla: intentar mapear el objeto como un solo plan
            try {
                FolioPlan single = mapper.readValue(body, FolioPlan.class);
                log.info("[FOLIO-PLANS] Body representa un solo plan, se devolverá respuesta con 1 elemento (id={})", single.getId());
                FolioPlanResponse response = new FolioPlanResponse();
                response.setResults(Collections.singletonList(single));
                response.setCount(1);
                response.setCurrentPage(1);
                response.setTotalPages(1);
                response.setPageSize(1);
                response.setHasNext(false);
                response.setHasPrevious(false);
                return response;
            } catch (Exception ex) {
                // Mostrar claves para diagnóstico
                Iterator<String> fieldNames = root.fieldNames();
                StringBuilder sb = new StringBuilder();
                while (fieldNames.hasNext()) {
                    sb.append(fieldNames.next()).append(',');
                }
                log.error("[FOLIO-PLANS] No se pudo identificar estructura de planes. Claves encontradas: {}", sb);
                return new FolioPlanResponse();
            }
        }

        log.warn("[FOLIO-PLANS] Formato inesperado de respuesta JSON");
        return new FolioPlanResponse();
    }

    private TenantFolioSubscriptionResponse parseSubscriptionResponse(String body) throws Exception {
        JsonNode root = mapper.readTree(body);
        TenantFolioSubscriptionResponse resp = new TenantFolioSubscriptionResponse();
        if (root.isObject() && root.has("results")) {
            resp = mapper.readValue(body, TenantFolioSubscriptionResponse.class);
            return resp;
        }
        if (root.isArray()) {
            List<TenantFolioSubscription> subs = mapper.readValue(body, new TypeReference<List<TenantFolioSubscription>>(){});
            resp.setResults(subs);
            resp.setCount(subs.size());
            resp.setCurrentPage(1);
            resp.setTotalPages(1);
            resp.setPageSize(subs.size());
            resp.setHasNext(false);
            resp.setHasPrevious(false);
            return resp;
        }
        // Intentar encontrar un array en campos comunes
        String[] candidates = {"data", "items", "subscriptions", "folio_subscriptions"};
        for (String c : candidates) {
            JsonNode node = root.get(c);
            if (node != null && node.isArray()) {
                List<TenantFolioSubscription> subs = mapper.readValue(node.toString(), new TypeReference<List<TenantFolioSubscription>>(){});
                resp.setResults(subs);
                resp.setCount(subs.size());
                resp.setCurrentPage(1);
                resp.setTotalPages(1);
                resp.setPageSize(subs.size());
                resp.setHasNext(false);
                resp.setHasPrevious(false);
                return resp;
            }
        }
        return resp; // vacío
    }

    private String getBaseUrl() {
        String tenantsBaseUrl = config.getAllTenantsBaseUrl();
        if (tenantsBaseUrl == null || tenantsBaseUrl.trim().isEmpty()) {
            throw new IllegalStateException("URL base de tenants no configurada");
        }
        return tenantsBaseUrl.endsWith("/") ? tenantsBaseUrl.substring(0, tenantsBaseUrl.length() - 1) : tenantsBaseUrl;
    }
}
