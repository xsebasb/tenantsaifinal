package com.gruposai.tenantmanager.DTO;

import lombok.Data;

@Data
public class TenantRequest {
    private String document;
    private int verification_digit;
    private String company_name;
    private String company_email;
    private String whatsapp_number;
    private String company_address;
    private String company_phone;

}
