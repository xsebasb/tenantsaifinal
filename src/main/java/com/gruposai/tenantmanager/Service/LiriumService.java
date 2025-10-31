package com.gruposai.tenantmanager.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gruposai.tenantmanager.DTO.*;
import com.gruposai.tenantmanager.Util.LiriumConfig;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Collections;

@Service
public class LiriumService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final LiriumConfig config;
    private final ObjectMapper mapper = new ObjectMapper();

    public LiriumService() {
        this.config = new LiriumConfig(); // Cargar configuración al iniciar
    }

    public String obtenerToken() {
        AuthRequest auth = new AuthRequest();
        auth.setUsername(config.getUsername());
        auth.setPassword(config.getPassword());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<AuthRequest> request = new HttpEntity<>(auth, headers);

        ResponseEntity<AuthResponse> response = restTemplate.exchange(
                config.getTokenUrl(), HttpMethod.POST, request, AuthResponse.class
        );

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody().getAccess();
        } else {
            throw new RuntimeException("Error al obtener token: " + response.getStatusCode());
        }
    }

    public TenantCreationResponse crearTenant(TenantRequest tenant) {
        String token = obtenerToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<TenantRequest> request = new HttpEntity<>(tenant, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                config.getTenantUrl(), HttpMethod.POST, request, String.class
        );

        if (response.getStatusCodeValue() == 202) {
            try {
                return mapper.readValue(response.getBody(), TenantCreationResponse.class);
            } catch (Exception e) {
                throw new RuntimeException("Error al parsear la respuesta de creación de tenant", e);
            }
        } else {
            throw new RuntimeException("Error al crear el tenant: " + response.getBody());
        }
    }

    // ---------- Métodos de lectura ----------

    private HttpHeaders buildAuthHeaders() {
        String token = obtenerToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }

    private URI buildUri(String path, MultiValueMap<String, String> params) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(path);
        if (params != null) {
            params.forEach((k, v) -> v.forEach(val -> builder.queryParam(k, val)));
        }
        return builder.build(true).toUri();
    }

    public TenantListResponse listarTenants(MultiValueMap<String, String> params) {
        String base = ensureBase();
        URI uri = buildUri(base, params);
        ResponseEntity<String> resp = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(buildAuthHeaders()), String.class);
        if (!resp.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Error al listar tenants: " + resp.getStatusCode());
        }
        try {
            return mapper.readValue(resp.getBody(), TenantListResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Error al parsear listado de tenants", e);
        }
    }

    public List<TenantLookupItem> lookupTenants(MultiValueMap<String, String> params) {
        String url = ensureBase() + "lookup/";
        URI uri = buildUri(url, params);
        ResponseEntity<String> resp = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(buildAuthHeaders()), String.class);
        if (!resp.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Error en lookup tenants: " + resp.getStatusCode());
        }
        try {
            return mapper.readValue(resp.getBody(), new TypeReference<List<TenantLookupItem>>(){});
        } catch (Exception e) {
            throw new RuntimeException("Error al parsear lookup tenants", e);
        }
    }

    public List<TenantAutocompleteItem> searchAutocomplete(MultiValueMap<String, String> params) {
        String url = ensureBase() + "search_autocomplete/";
        URI uri = buildUri(url, params);
        ResponseEntity<String> resp = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(buildAuthHeaders()), String.class);
        if (!resp.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Error en search_autocomplete: " + resp.getStatusCode());
        }
        try {
            return mapper.readValue(resp.getBody(), new TypeReference<List<TenantAutocompleteItem>>(){});
        } catch (Exception e) {
            throw new RuntimeException("Error al parsear search_autocomplete", e);
        }
    }

    public JsonNode obtenerTenantDetalle(Long id) {
        String url = ensureBase() + id + "/";
        ResponseEntity<String> resp = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(buildAuthHeaders()), String.class);
        if (!resp.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Error al obtener tenant: " + resp.getStatusCode());
        }
        try {
            return mapper.readTree(resp.getBody());
        } catch (Exception e) {
            throw new RuntimeException("Error al parsear detalle de tenant", e);
        }
    }

    public TenantSummary obtenerTenantSummary(Long id) {
        String url = ensureBase() + id + "/summary/";
        ResponseEntity<String> resp = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(buildAuthHeaders()), String.class);
        if (!resp.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Error al obtener summary: " + resp.getStatusCode());
        }
        try {
            return mapper.readValue(resp.getBody(), TenantSummary.class);
        } catch (Exception e) {
            throw new RuntimeException("Error al parsear summary de tenant", e);
        }
    }

    public TenantStats obtenerStats() {
        String url = ensureBase() + "stats/";
        ResponseEntity<String> resp = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(buildAuthHeaders()), String.class);
        if (!resp.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Error al obtener stats: " + resp.getStatusCode());
        }
        try {
            return mapper.readValue(resp.getBody(), TenantStats.class);
        } catch (Exception e) {
            throw new RuntimeException("Error al parsear stats", e);
        }
    }

    private String ensureBase() {
        String base = config.getTenantsBaseUrl();
        if (base == null || base.trim().isEmpty()) {
            throw new IllegalStateException("URL base de tenants no configurada (url.tenants-base en config.cfg)");
        }
        if (!base.endsWith("/")) {
            base = base + "/";
        }
        return base;
    }
}
