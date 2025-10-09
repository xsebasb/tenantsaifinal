package com.gruposai.tenantmanager.Controller;

import com.gruposai.tenantmanager.DTO.CustomerProjection;
import com.gruposai.tenantmanager.DTO.TenantCreationResponse;
import com.gruposai.tenantmanager.DTO.TenantRequest;
import com.gruposai.tenantmanager.Service.CustomerService;
import com.gruposai.tenantmanager.Service.LiriumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/tenants")
public class TenantMvcController {

    private final LiriumService liriumService;

    @Autowired
    private CustomerService customerService;
    @Autowired
    public TenantMvcController(LiriumService liriumService) {
        this.liriumService = liriumService;
    }

    @GetMapping("/crear")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("tenantRequest", new TenantRequest());
        return "crear-tenant";
    }

    @PostMapping("/crear")
    public String procesarFormularioCrear(@ModelAttribute TenantRequest tenantRequest, Model model) {
        try {
            TenantCreationResponse resp = liriumService.crearTenant(tenantRequest);

            String companyId = resp.getCompany_id();
            String mensaje = String.format(
                    "Tenant creado exitosamente.<br>" +
                            "Correo: <b>%s@fe.binariotic.com</b><br>" +
                            "Usuario: <b>%s</b><br>" +
                            "URL: <b>https://%s.liriumsas.app/admin</b>",
                    companyId, companyId, companyId
            );

            model.addAttribute("mensaje", mensaje);
        } catch (Exception e) {
            model.addAttribute("mensaje", "Error al crear el tenant: " + e.getMessage());
        }
        return "crear-tenant";
    }

    @ResponseBody
    @GetMapping("/buscar-por-documento")
    public List<CustomerProjection> buscarPorDocumento(@RequestParam("documento") String documento) {
        return customerService.findCustomerByIdN(documento);
    }
}
