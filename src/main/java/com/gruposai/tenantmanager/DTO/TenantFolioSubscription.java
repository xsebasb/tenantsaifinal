package com.gruposai.tenantmanager.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TenantFolioSubscription {
    private Integer id;
    private Integer tenant;
    private Integer plan;

    @JsonProperty("tenant_detail")
    private TenantDetail tenantDetail;

    @JsonProperty("plan_detail")
    private FolioPlan planDetail;

    @JsonProperty("folios_purchased")
    private Integer foliosPurchased;

    @JsonProperty("folios_consumed")
    private Integer foliosConsumed;

    @JsonProperty("folios_remaining")
    private Integer foliosRemaining;

    @JsonProperty("purchase_date")
    private String purchaseDate;

    @JsonProperty("start_date")
    private String startDate;

    @JsonProperty("expiry_date")
    private String expiryDate;

    @JsonProperty("is_active")
    private Boolean isActive;

    @JsonProperty("order_reference")
    private String orderReference;

    @JsonProperty("auto_renew")
    private Boolean autoRenew;

    @JsonProperty("status_display")
    private String statusDisplay;

    @JsonProperty("days_until_expiry")
    private Integer daysUntilExpiry;

    @JsonProperty("usage_percentage")
    private Double usagePercentage;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("updated_at")
    private String updatedAt;

    public TenantFolioSubscription() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getTenant() { return tenant; }
    public void setTenant(Integer tenant) { this.tenant = tenant; }
    public Integer getPlan() { return plan; }
    public void setPlan(Integer plan) { this.plan = plan; }
    public TenantDetail getTenantDetail() { return tenantDetail; }
    public void setTenantDetail(TenantDetail tenantDetail) { this.tenantDetail = tenantDetail; }
    public FolioPlan getPlanDetail() { return planDetail; }
    public void setPlanDetail(FolioPlan planDetail) { this.planDetail = planDetail; }
    public Integer getFoliosPurchased() { return foliosPurchased; }
    public void setFoliosPurchased(Integer foliosPurchased) { this.foliosPurchased = foliosPurchased; }
    public Integer getFoliosConsumed() { return foliosConsumed; }
    public void setFoliosConsumed(Integer foliosConsumed) { this.foliosConsumed = foliosConsumed; }
    public Integer getFoliosRemaining() { return foliosRemaining; }
    public void setFoliosRemaining(Integer foliosRemaining) { this.foliosRemaining = foliosRemaining; }
    public String getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(String purchaseDate) { this.purchaseDate = purchaseDate; }
    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public String getExpiryDate() { return expiryDate; }
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public String getOrderReference() { return orderReference; }
    public void setOrderReference(String orderReference) { this.orderReference = orderReference; }
    public Boolean getAutoRenew() { return autoRenew; }
    public void setAutoRenew(Boolean autoRenew) { this.autoRenew = autoRenew; }
    public String getStatusDisplay() { return statusDisplay; }
    public void setStatusDisplay(String statusDisplay) { this.statusDisplay = statusDisplay; }
    public Integer getDaysUntilExpiry() { return daysUntilExpiry; }
    public void setDaysUntilExpiry(Integer daysUntilExpiry) { this.daysUntilExpiry = daysUntilExpiry; }
    public Double getUsagePercentage() { return usagePercentage; }
    public void setUsagePercentage(Double usagePercentage) { this.usagePercentage = usagePercentage; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TenantDetail {
        private Integer id;
        @JsonProperty("schema_name")
        private String schemaName;
        private String name;
        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }
        public String getSchemaName() { return schemaName; }
        public void setSchemaName(String schemaName) { this.schemaName = schemaName; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    @Override
    public String toString() {
        return "TenantFolioSubscription{" +
                "id=" + id +
                ", tenant=" + tenant +
                ", plan=" + plan +
                ", foliosPurchased=" + foliosPurchased +
                ", foliosConsumed=" + foliosConsumed +
                ", foliosRemaining=" + foliosRemaining +
                ", purchaseDate='" + purchaseDate + '\'' +
                ", startDate='" + startDate + '\'' +
                ", expiryDate='" + expiryDate + '\'' +
                ", isActive=" + isActive +
                ", orderReference='" + orderReference + '\'' +
                ", autoRenew=" + autoRenew +
                ", statusDisplay='" + statusDisplay + '\'' +
                ", daysUntilExpiry=" + daysUntilExpiry +
                ", usagePercentage=" + usagePercentage +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }
}
