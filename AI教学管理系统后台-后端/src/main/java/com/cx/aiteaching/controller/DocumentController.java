package com.cx.aiteaching.controller;

import com.cx.aiteaching.dto.UploadResponse;
import com.cx.aiteaching.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private static final Logger logger = LoggerFactory.getLogger(DocumentController.class);

    @Autowired
    private DocumentService documentService;

    @PostMapping("/upload")
    public ResponseEntity<UploadResponse> uploadDocument(@RequestParam("file") MultipartFile file) {
        try {
            logger.info("Received file: {}", file.getOriginalFilename());
            UploadResponse response = documentService.uploadDocument(file);
            logger.info("Upload response: {}", response);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error in uploadDocument", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/summarize")
    public ResponseEntity<String> summarizeDocument(@RequestParam("fileId") String fileId) {
        try {
            String summary = documentService.summarizeDocument(fileId);
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            logger.error("Error in summarizeDocument", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


}
