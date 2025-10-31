package com.gruposai.tenantmanager.DTO;

import lombok.Data;

@Data
public class TenantLookupItem {
    private String value;
    private String label;
    private String schema_name;
    private String name;
}

