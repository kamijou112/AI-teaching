package com.cx.aiteaching.service.impl;

import com.cx.aiteaching.dto.UploadResponse;
import com.cx.aiteaching.service.DocumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class DocumentServiceImpl implements DocumentService {

    private static final Logger logger = LoggerFactory.getLogger(DocumentServiceImpl.class);

    private static final String UPLOAD_URL = "https://chatdoc.xfyun.cn/openapi/v1/file/upload";
    private static final String SUMMARY_URL = "https://chatdoc.xfyun.cn/openapi/v1/file/summary/start";
    private static final String SUMMARY_QUERY_URL = "https://chatdoc.xfyun.cn/openapi/v1/file/summary/query";
    private static final String APP_ID = "67a0339c";
    private static final String API_SECRET = "YmQxNmQyOTA1MDU4YjIxODA1NmUzZDYx";

    @Override
    public UploadResponse uploadDocument(MultipartFile file) {
        RestTemplate restTemplate = new RestTemplate();

        long timestamp = System.currentTimeMillis() / 1000;
        String signature = generateSignature(APP_ID, API_SECRET, timestamp);

        HttpHeaders headers = new HttpHeaders();
        headers.add("appId", APP_ID);
        headers.add("timestamp", String.valueOf(timestamp));
        headers.add("signature", signature);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", file.getResource());

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        logger.info("Sending request to URL: {}", UPLOAD_URL);
        ResponseEntity<UploadResponse> response;
        try {
            response = restTemplate.exchange(UPLOAD_URL, HttpMethod.POST, requestEntity, UploadResponse.class);
            logger.info("Response received: {}", response.getBody());
        } catch (Exception e) {
            logger.error("Error during file upload", e);
            throw new RuntimeException("File upload failed", e);
        }
        return response.getBody();
    }



    @Override
    public String summarizeDocument(String fileId) {
        RestTemplate restTemplate = new RestTemplate();
        long timestamp = System.currentTimeMillis() / 1000;
        String signature = generateSignature(APP_ID, API_SECRET, timestamp);

        HttpHeaders headers = new HttpHeaders();
        headers.add("appId", APP_ID);
        headers.add("timestamp", String.valueOf(timestamp));
        headers.add("signature", signature);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("fileId", fileId);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        logger.info("Sending summary request to URL: {}", SUMMARY_URL);
        try {
            restTemplate.exchange(SUMMARY_URL, HttpMethod.POST, requestEntity, String.class);
        } catch (Exception e) {
            logger.error("Error during summarizeDocument", e);
            throw new RuntimeException("Summarize document failed", e);
        }

        // Query summary
        logger.info("Querying summary from URL: {}", SUMMARY_QUERY_URL);
        try {
            while (true) {
                ResponseEntity<String> response = restTemplate.exchange(SUMMARY_QUERY_URL, HttpMethod.POST, requestEntity, String.class);
                String responseBody = response.getBody();
                logger.info("Summary query response: {}", responseBody);

                if (responseBody.contains("\"summaryStatus\":\"done\"")) {
                    return responseBody;
                } else if (responseBody.contains("\"summaryStatus\":\"failed\"")) {
                    throw new RuntimeException("Document summarization failed");
                }

                // Wait for a few seconds before the next query
                Thread.sleep(3000);
            }
        } catch (Exception e) {
            logger.error("Error during summarizeDocument query", e);
            throw new RuntimeException("Summarize document query failed", e);
        }
    }


    private String generateSignature(String appId, String secret, long timestamp) {
        try {
            String auth = md5(appId + timestamp);
            return hmacSHA1Encrypt(auth, secret);
        } catch (Exception e) {
            logger.error("Error generating signature", e);
            return null;
        }
    }

    private String hmacSHA1Encrypt(String data, String key) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA1");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA1");
        mac.init(secretKey);
        byte[] bytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(bytes);
    }

    private String md5(String data) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] array = md.digest(data.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : array) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
