package com.cx.aiteaching.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.cx.aiteaching.service.AudioModel;

@RestController
@RequestMapping("/upload")
public class AudioUploadController {

    private static final Logger logger = LoggerFactory.getLogger(AudioUploadController.class);

    @Autowired
    private AudioModel b1;

    @PostMapping("/Audioouput")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }

        File convFile;
        try {
            // 保存文件
            convFile = new File("audio.pcm");
            try (FileOutputStream fos = new FileOutputStream(convFile)) {
                fos.write(file.getBytes());
            }

            String result = b1.AM(convFile);

            return ResponseEntity.ok(result);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error saving file");
        }
    }
}