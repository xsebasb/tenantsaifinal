package com.gruposai.tenantmanager.Controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.gruposai.tenantmanager.DTO.*;
import com.gruposai.tenantmanager.Service.CustomerService;
import com.gruposai.tenantmanager.Service.LiriumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tenants-api")
public class TenantController {

    private final LiriumService liriumService;
    @Autowired
    private CustomerService customerService;

    @Autowired
    public TenantController(LiriumService liriumService) {
        this.liriumService = liriumService;
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crearTenant(@RequestBody TenantRequest request) {
        try {
            TenantCreationResponse resp = liriumService.crearTenant(request);
            return ResponseEntity.status(202).body(resp);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(error("Error al crear tenant", e));
        }
    }

    @GetMapping("/token")
    public ResponseEntity<?> obtenerToken() {
        try {
            String token = liriumService.obtenerToken();
            return ResponseEntity.ok(Collections.singletonMap("access", token));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(error("Error al obtener token", e));
        }
    }

    @GetMapping("/by-idn/{id_n}")
    public List<?> getCustomerByIdN(@PathVariable("id_n") String id_n) {
        return customerService.findCustomerByIdN(id_n);
    }

    // ----------- Endpoints de lectura proxys a la API externa -----------

    @GetMapping
    public ResponseEntity<?> listTenants(@RequestParam(required = false) MultiValueMap<String,String> params) {
        try {
            MultiValueMap<String,String> p = params != null ? params : new LinkedMultiValueMap<>();
            return ResponseEntity.ok(liriumService.listarTenants(p));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(error("Error listando tenants", e));
        }
    }

    @GetMapping("/lookup")
    public ResponseEntity<?> lookup(@RequestParam(required = false) MultiValueMap<String,String> params) {
        try {
            MultiValueMap<String,String> p = params != null ? params : new LinkedMultiValueMap<>();
            return ResponseEntity.ok(liriumService.lookupTenants(p));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(error("Error en lookup", e));
        }
    }

    @GetMapping("/search_autocomplete")
    public ResponseEntity<?> searchAutocomplete(@RequestParam(required = false) MultiValueMap<String,String> params) {
        try {
            MultiValueMap<String,String> p = params != null ? params : new LinkedMultiValueMap<>();
            return ResponseEntity.ok(liriumService.searchAutocomplete(p));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(error("Error en autocomplete", e));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTenant(@PathVariable Long id) {
        try {
            JsonNode node = liriumService.obtenerTenantDetalle(id);
            return ResponseEntity.ok(node);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(error("Error obteniendo tenant", e));
        }
    }

    @GetMapping("/{id}/summary")
    public ResponseEntity<?> getTenantSummary(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(liriumService.obtenerTenantSummary(id));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(error("Error obteniendo summary", e));
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<?> stats() {
        try {
            return ResponseEntity.ok(liriumService.obtenerStats());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(error("Error obteniendo stats", e));
        }
    }

    private Map<String,Object> error(String msg, Exception e) {
        Map<String,Object> m = new HashMap<>();
        m.put("error", msg);
        m.put("detail", e.getMessage());
        return m;
    }
}