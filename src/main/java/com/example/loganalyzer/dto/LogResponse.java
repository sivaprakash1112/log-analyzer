package com.example.loganalyzer.dto;

import java.util.Map;

public class LogResponse {

    private long totalRequests;
    private long totalBytes;
    private Map<String, Integer> topUrls;
    private Map<Integer, Integer> statusCount;
    private long processingTimeMs;

    public LogResponse(long totalRequests,
                       long totalBytes,
                       Map<String, Integer> topUrls,
                       Map<Integer, Integer> statusCount,
                       long processingTimeMs) {

        this.totalRequests = totalRequests;
        this.totalBytes = totalBytes;
        this.topUrls = topUrls;
        this.statusCount = statusCount;
        this.processingTimeMs = processingTimeMs;
    }

    public long getTotalRequests() {
        return totalRequests;
    }

    public long getTotalBytes() {
        return totalBytes;
    }

    public Map<String, Integer> getTopUrls() {
        return topUrls;
    }

    public Map<Integer, Integer> getStatusCount() {
        return statusCount;
    }

    public long getProcessingTimeMs() {
        return processingTimeMs;
    }
}