package com.cx.aiteaching.controller;

import com.cx.aiteaching.service.SpeechRecognitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/speech")
public class SpeechRecognitionController {

    @Autowired
    private SpeechRecognitionService speechRecognitionService;

    @PostMapping("/recognize")
    public String recognizeSpeech(@RequestParam("file") MultipartFile file) throws IOException {
        byte[] audioData = file.getBytes();
        return speechRecognitionService.recognizeSpeech(audioData);
    }
}
