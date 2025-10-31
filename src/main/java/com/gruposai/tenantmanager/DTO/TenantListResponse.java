package com.gruposai.tenantmanager.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class TenantListResponse {
    private int count;
    private String next;
    private String previous;
    private List<TenantListItem> results;

    // Propiedades calculadas para la paginación
    @JsonProperty("total_pages")
    private Integer totalPages;

    @JsonProperty("current_page")
    private Integer currentPage;

    @JsonProperty("page_size")
    private Integer pageSize;

    // Constructors
    public TenantListResponse() {}

    public TenantListResponse(int count, String next, String previous, List<TenantListItem> results) {
        this.count = count;
        this.next = next;
        this.previous = previous;
        this.results = results;
    }

    // Getters and Setters
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public List<TenantListItem> getResults() {
        return results;
    }

    public void setResults(List<TenantListItem> results) {
        this.results = results;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    // Método para calcular las páginas totales si no viene en la respuesta
    public void calculatePagination() {
        if (this.pageSize != null && this.pageSize > 0) {
            this.totalPages = (int) Math.ceil((double) this.count / this.pageSize);
        }
    }
}
