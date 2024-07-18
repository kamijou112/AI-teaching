package com.cx.aiteaching.controller;

import com.cx.aiteaching.service.ImageAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/image")
public class ImageController {

    @Autowired
    private ImageAnalysisService imageAnalysisService;

    @PostMapping("/analyze")
    public ResponseEntity<String> analyzeImage(@RequestParam("file") MultipartFile file, @RequestParam("question") String question) {
        try {
            List<String> analysisResults = Collections.singletonList(imageAnalysisService.analyzeImage(file, question));
            JSONObject responseJson = new JSONObject();
            responseJson.put("status", "success");
            responseJson.put("answer", String.join(" ", analysisResults));
            return ResponseEntity.ok(responseJson.toString());
        } catch (IOException e) {
            JSONObject errorJson = new JSONObject();
            errorJson.put("status", "error");
            errorJson.put("message", "Error processing image: " + e.getMessage());
            return ResponseEntity.status(500).body(errorJson.toString());
        }
    }
}