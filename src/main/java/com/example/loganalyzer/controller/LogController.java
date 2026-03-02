package com.example.loganalyzer.controller;

import com.example.loganalyzer.dto.LogResponse;
import com.example.loganalyzer.model.LogResult;
import com.example.loganalyzer.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/logs")
@CrossOrigin
public class LogController {

    @Autowired
    private LogService logService;



    @PostMapping("/upload")
    public LogResponse uploadLog(
            @RequestParam("file") MultipartFile file,
            @RequestParam(required = false) Integer status)
            throws Exception {

        return logService.processLog(file, status);
    }
}