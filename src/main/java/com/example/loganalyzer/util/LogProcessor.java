package com.example.loganalyzer.util;

import com.example.loganalyzer.util.LogProcessor;

import com.example.loganalyzer.model.LogResult;

public class LogProcessor implements Runnable {

    private final String line;
    private final LogResult result;
    private final Integer filterStatus;

    public LogProcessor(String line, LogResult result, Integer filterStatus) {
        this.line = line;
        this.result = result;
        this.filterStatus = filterStatus;
    }

    @Override
    public void run() {
        processLine(line);
    }

    private void processLine(String line) {


        try {

            if (line == null || line.isEmpty()) {
                return;
            }

            int firstQuote = line.indexOf("\"");
            int secondQuote = line.indexOf("\"", firstQuote + 1);

            if (firstQuote == -1 || secondQuote == -1) {
                return;
            }

            String requestPart = line.substring(firstQuote + 1, secondQuote);
            String[] requestParts = requestPart.split(" ");

            if (requestParts.length < 2) {
                return;
            }

            String url = requestParts[1];

            String remaining = line.substring(secondQuote + 2);
            String[] remainingParts = remaining.split(" ");

            if (remainingParts.length < 2) {
                return;
            }

            int status = Integer.parseInt(remainingParts[0]);


            // 🔥 FILTER LOGIC (PUT HERE)
            if (filterStatus != null && !filterStatus.equals(status)) {
                return;
            }

            long bytes = 0;
            if (!remainingParts[1].equals("-")) {
                bytes = Long.parseLong(remainingParts[1]);
            }

            result.addUrl(url);
            result.addStatus(status);
            result.addBytes(bytes);
            result.incrementRequests();

        } catch (Exception e) {
            e.printStackTrace(); // TEMP: show error in console
        }
    }
}