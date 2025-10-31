package com.gruposai.tenantmanager.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TenantListItem {
    private Long id;
    private String schema_name;
    private String name;
    private String paid_until;
    private Boolean on_trial;
    private String created_on;
    private String company_name;
    private String company_document;
    private Boolean is_active;
    private Boolean is_expired;
    @JsonProperty("_str")
    private String str;
    @JsonProperty("_status_display")
    private String statusDisplay;
}

