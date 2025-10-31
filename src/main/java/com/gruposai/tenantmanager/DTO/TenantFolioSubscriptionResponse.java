package com.gruposai.tenantmanager.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TenantFolioSubscriptionResponse {
    private Integer count;
    private String next;
    private String previous;

    @JsonProperty("current_page")
    private Integer currentPage;
    @JsonProperty("total_pages")
    private Integer totalPages;
    @JsonProperty("page_size")
    private Integer pageSize;
    @JsonProperty("has_next")
    private Boolean hasNext;
    @JsonProperty("has_previous")
    private Boolean hasPrevious;

    private List<TenantFolioSubscription> results;

    public Integer getCount() { return count; }
    public void setCount(Integer count) { this.count = count; }
    public String getNext() { return next; }
    public void setNext(String next) { this.next = next; }
    public String getPrevious() { return previous; }
    public void setPrevious(String previous) { this.previous = previous; }
    public Integer getCurrentPage() { return currentPage; }
    public void setCurrentPage(Integer currentPage) { this.currentPage = currentPage; }
    public Integer getTotalPages() { return totalPages; }
    public void setTotalPages(Integer totalPages) { this.totalPages = totalPages; }
    public Integer getPageSize() { return pageSize; }
    public void setPageSize(Integer pageSize) { this.pageSize = pageSize; }
    public Boolean getHasNext() { return hasNext; }
    public void setHasNext(Boolean hasNext) { this.hasNext = hasNext; }
    public Boolean getHasPrevious() { return hasPrevious; }
    public void setHasPrevious(Boolean hasPrevious) { this.hasPrevious = hasPrevious; }
    public List<TenantFolioSubscription> getResults() { return results; }
    public void setResults(List<TenantFolioSubscription> results) { this.results = results; }

    @Override
    public String toString() {
        return "TenantFolioSubscriptionResponse{" +
                "count=" + count +
                ", next='" + next + '\'' +
                ", previous='" + previous + '\'' +
                ", currentPage=" + currentPage +
                ", totalPages=" + totalPages +
                ", pageSize=" + pageSize +
                ", hasNext=" + hasNext +
                ", hasPrevious=" + hasPrevious +
                ", results=" + (results!=null? results.size():0) +
                '}';
    }
}

