package com.example.loganalyzer.model;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class LogResult {

    private ConcurrentHashMap<String, AtomicInteger> urlCount = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Integer, AtomicInteger> statusCount = new ConcurrentHashMap<>();
    private AtomicLong totalBytes = new AtomicLong(0);
    private AtomicInteger totalRequests = new AtomicInteger(0);

    public void addUrl(String url) {
        urlCount.computeIfAbsent(url, k -> new AtomicInteger(0)).incrementAndGet();
    }

    public void addStatus(int status) {
        statusCount.computeIfAbsent(status, k -> new AtomicInteger(0)).incrementAndGet();
    }

    public void addBytes(long bytes) {
        totalBytes.addAndGet(bytes);
    }

    public void incrementRequests() {
        totalRequests.incrementAndGet();
    }

    public ConcurrentHashMap<String, AtomicInteger> getUrlCount() {
        return urlCount;
    }

    public ConcurrentHashMap<Integer, AtomicInteger> getStatusCount() {
        return statusCount;
    }

    public long getTotalBytes() {
        return totalBytes.get();
    }

    public int getTotalRequests() {
        return totalRequests.get();
    }
}
