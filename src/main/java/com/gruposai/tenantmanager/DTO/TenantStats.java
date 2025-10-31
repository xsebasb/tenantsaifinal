package com.gruposai.tenantmanager.DTO;

import lombok.Data;

@Data
public class TenantStats {
    private Integer total;
    private Integer active;
    private Integer on_trial;
    private Integer expired;
    private Integer expiring_soon;
}

