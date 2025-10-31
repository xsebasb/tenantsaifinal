package com.gruposai.tenantmanager.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FolioPlanResponse {

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

    private List<FolioPlan> results;

    // Constructor
    public FolioPlanResponse() {}

    // Getters and Setters
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

    public List<FolioPlan> getResults() { return results; }
    public void setResults(List<FolioPlan> results) { this.results = results; }

    @Override
    public String toString() {
        return "FolioPlanResponse{" +
                "count=" + count +
                ", next='" + next + '\'' +
                ", previous='" + previous + '\'' +
                ", currentPage=" + currentPage +
                ", totalPages=" + totalPages +
                ", pageSize=" + pageSize +
                ", hasNext=" + hasNext +
                ", hasPrevious=" + hasPrevious +
                ", results=" + results +
                '}';
    }
}
