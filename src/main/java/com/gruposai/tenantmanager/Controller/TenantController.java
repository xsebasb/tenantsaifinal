package com.gruposai.tenantmanager.Controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gruposai.tenantmanager.DTO.CustomerProjection;
import com.gruposai.tenantmanager.DTO.TenantCreationResponse;
import com.gruposai.tenantmanager.DTO.TenantRequest;
import com.gruposai.tenantmanager.Service.CustomerService;
import com.gruposai.tenantmanager.Service.LiriumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<TenantCreationResponse> crearTenant(@RequestBody TenantRequest request) {
        try {
            TenantCreationResponse resp = liriumService.crearTenant(request);
            return ResponseEntity.status(202).body(resp);
        } catch (Exception e) {
            // Puedes crear un objeto de error o retornar un mensaje simple
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/token")
    public ResponseEntity<String> obtenerToken() {
        try {
            String token = liriumService.obtenerToken();
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error al obtener el token: " + e.getMessage());
        }
    }

    @GetMapping("/by-idn/{id_n}")
    public List<?> getCustomerByIdN(@PathVariable("id_n") String id_n) {
        return customerService.findCustomerByIdN(id_n);
    }

}