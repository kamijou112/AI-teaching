package com.cx.aiteaching.controller;

import com.cx.aiteaching.dto.ExpertRequirementsDto;
import com.cx.aiteaching.service.ExpertModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/Expert")
public class ExpertModelController {

    @Autowired
    private ExpertModelService expertModelService;

    @PostMapping("/Expert1")
    public ResponseEntity<String> ExpertOutput(@RequestBody ExpertRequirementsDto requirementsDto) throws IOException {
        String studentResult = requirementsDto.getStudentResult();
        String type = requirementsDto.getType();
        String output = expertModelService.ExpertModeloutput(studentResult, type);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        return new ResponseEntity<>(output, headers, HttpStatus.OK);
    }
}
