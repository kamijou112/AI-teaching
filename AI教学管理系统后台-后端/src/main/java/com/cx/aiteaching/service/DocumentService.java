package com.cx.aiteaching.service;

import com.cx.aiteaching.dto.UploadResponse;
import org.springframework.web.multipart.MultipartFile;

public interface DocumentService {
    UploadResponse uploadDocument(MultipartFile file);
    String summarizeDocument(String fileId);

}
