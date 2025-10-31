package com.gruposai.tenantmanager.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TenantSummary {
    private Long id;
    private String schema_name;
    private String name;
    private String paid_until;
    private Boolean on_trial;
    private String created_on;
    private CompanyInfo company_info;
    private Boolean is_active;
    private Boolean is_expired;
    private Integer days_until_expiration;
    private TrialStatus trial_status;
    private UsageSummary usage_summary;
    @JsonProperty("_str")
    private String str;
    @JsonProperty("_status_display")
    private String status_display;

    @Data
    public static class CompanyInfo {
        private String document;
        private String name;
        private String email;
        private String phone;
        private String city;
    }

    @Data
    public static class TrialStatus {
        private Boolean is_trial;
        private Integer days_remaining;
        private String expires_on;
    }

    @Data
    public static class UsageSummary {
        private Integer invoices_count;
        private Integer users_count;
        private Long storage_used;
        private String last_activity;
    }
}


