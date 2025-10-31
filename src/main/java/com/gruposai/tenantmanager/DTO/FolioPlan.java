package com.gruposai.tenantmanager.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class FolioPlan{
    private Integer id;
    private String name;
    private String description;
    private String negociation;

    @JsonProperty("folios_included")
    private Integer foliosIncluded;

    // Cambiar de Double a String para coincidir con el JSON
    private String price;

    private Integer distributor;

    @JsonProperty("distributor_name")
    private String distributorName;

    @JsonProperty("folios_display")
    private String foliosDisplay;

    @JsonProperty("price_display")
    private String priceDisplay;

    // Corregir para que coincida exactamente con el JSON (sin anotación @JsonProperty)
    private Boolean active;

    // Cambiar nombres para coincidir exactamente con el JSON
    private String created;
    private String updated;

    // Constructors
    public FolioPlan() {}

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getNegociation() { return negociation; }
    public void setNegociation(String negociation) { this.negociation = negociation; }

    public Integer getFoliosIncluded() { return foliosIncluded; }
    public void setFoliosIncluded(Integer foliosIncluded) { this.foliosIncluded = foliosIncluded; }

    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }

    public Integer getDistributor() { return distributor; }
    public void setDistributor(Integer distributor) { this.distributor = distributor; }

    public String getDistributorName() { return distributorName; }
    public void setDistributorName(String distributorName) { this.distributorName = distributorName; }

    public String getFoliosDisplay() { return foliosDisplay; }
    public void setFoliosDisplay(String foliosDisplay) { this.foliosDisplay = foliosDisplay; }

    public String getPriceDisplay() { return priceDisplay; }
    public void setPriceDisplay(String priceDisplay) { this.priceDisplay = priceDisplay; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public String getCreated() { return created; }
    public void setCreated(String created) { this.created = created; }

    public String getUpdated() { return updated; }
    public void setUpdated(String updated) { this.updated = updated; }

    @Override
    public String toString() {
        return "FolioPlan{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", negociation='" + negociation + '\'' +
                ", foliosIncluded=" + foliosIncluded +
                ", price='" + price + '\'' +
                ", distributor=" + distributor +
                ", distributorName='" + distributorName + '\'' +
                ", foliosDisplay='" + foliosDisplay + '\'' +
                ", priceDisplay='" + priceDisplay + '\'' +
                ", active=" + active +
                ", created='" + created + '\'' +
                ", updated='" + updated + '\'' +
                '}';
    }
}
