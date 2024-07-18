package com.cx.aiteaching.controller;

import com.cx.aiteaching.dto.StudentWrongQuestionsDTO;
import com.cx.aiteaching.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/wrongQuestions/{studentId}")
    public List<StudentWrongQuestionsDTO> getStudentWrongQuestions(@PathVariable int studentId) {
        return questionService.getStudentWrongQuestions(studentId);
    }
}
