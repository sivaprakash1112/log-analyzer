package com.example.loganalyzer.service;


import com.example.loganalyzer.dto.LogResponse;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

import com.example.loganalyzer.model.LogResult;
import com.example.loganalyzer.util.LogProcessor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class LogService {

    private static final int THREADS = Runtime.getRuntime().availableProcessors();

    public LogResponse processLog(MultipartFile file, Integer filterStatus) throws Exception {


        // 🔥 FILE SIZE VALIDATION (PUT HERE)
        if (file.getSize() > 1024L * 1024L * 100L) { // 100MB
            throw new RuntimeException("File too large! Max allowed 100MB");
        }

        long start = System.currentTimeMillis();
        LogResult result = new LogResult();
        ExecutorService executor = Executors.newFixedThreadPool(THREADS);


        BufferedReader reader =
                new BufferedReader(new InputStreamReader(file.getInputStream()));

        String line;

        while ((line = reader.readLine()) != null) {
            String logLine = line;

            executor.submit(new LogProcessor(logLine, result, filterStatus));
        }

        executor.shutdown();
        while (!executor.isTerminated()) {
            Thread.sleep(100);
        }

        long end = System.currentTimeMillis();
        long processingTime = end - start;

        // Convert Atomic maps to normal maps
        Map<String, Integer> sortedUrls =
                result.getUrlCount().entrySet()
                        .stream()
                        .sorted((a, b) -> b.getValue().get() - a.getValue().get())
                        .limit(3)
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                e -> e.getValue().get(),
                                (e1, e2) -> e1,
                                LinkedHashMap::new
                        ));

        Map<Integer, Integer> statusMap =
                result.getStatusCount().entrySet()
                        .stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                e -> e.getValue().get()
                        ));

        return new LogResponse(
                result.getTotalRequests(),
                result.getTotalBytes(),
                sortedUrls,
                statusMap,
                processingTime
        );
    }

    private void processLine(String line, LogResult result) {

        try {
            int firstQuote = line.indexOf("\"");
            int secondQuote = line.indexOf("\"", firstQuote + 1);

            String requestPart = line.substring(firstQuote + 1, secondQuote);
            String[] requestParts = requestPart.split(" ");
            String url = requestParts[1];

            String remaining = line.substring(secondQuote + 2);
            String[] remainingParts = remaining.split(" ");

            int status = Integer.parseInt(remainingParts[0]);
            long bytes = Long.parseLong(remainingParts[1]);

            result.addUrl(url);
            result.addStatus(status);
            result.addBytes(bytes);
            result.incrementRequests();

        } catch (Exception ignored) {
        }
    }
}
