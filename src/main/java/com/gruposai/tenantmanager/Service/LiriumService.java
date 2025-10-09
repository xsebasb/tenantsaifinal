package com.gruposai.tenantmanager.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gruposai.tenantmanager.DTO.AuthRequest;
import com.gruposai.tenantmanager.DTO.AuthResponse;
import com.gruposai.tenantmanager.DTO.TenantCreationResponse;
import com.gruposai.tenantmanager.DTO.TenantRequest;
import com.gruposai.tenantmanager.Util.LiriumConfig;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LiriumService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final LiriumConfig config;

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

        if (response.getStatusCode().is2xxSuccessful()) {
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
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(response.getBody(), TenantCreationResponse.class);
            } catch (Exception e) {
                throw new RuntimeException("Error al parsear la respuesta de creación de tenant", e);
            }
        } else {
            throw new RuntimeException("Error al crear el tenant: " + response.getBody());
        }
    }
}
