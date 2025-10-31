package com.gruposai.tenantmanager.DTO;

import lombok.Data;

@Data
public class TenantAutocompleteItem {
    private Long id;
    private String schema_name;
    private String name;
    private String paid_until;
    private Boolean on_trial;
    private Long company; // id de company
    private String company_name;
    private String company_document;
}

