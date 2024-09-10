package com.catis.objectTemporaire;

import java.util.List;
import java.util.Map;

public class PaginationResponse {
    private List<Map<String, Object>> content;
    private long totalElements;
    private int page;
    private int size;

    // Constructor
    public PaginationResponse(List<Map<String, Object>> content, long totalElements, int page, int size) {
        this.content = content;
        this.totalElements = totalElements;
        this.page = page;
        this.size = size;
    }

    // Getters and setters
    public List<Map<String, Object>> getContent() {
        return content;
    }

    public void setContent(List<Map<String, Object>> content) {
        this.content = content;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
