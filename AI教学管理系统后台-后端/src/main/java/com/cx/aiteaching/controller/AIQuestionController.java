package com.cx.aiteaching.controller;

import com.cx.aiteaching.entity.Question;
import com.cx.aiteaching.dto.RequirementsDto;
import com.cx.aiteaching.service.AIQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/ai")
public class AIQuestionController {

    @Autowired
    private AIQuestionService aiQuestionService;

    @PostMapping("/generateQuestions")
    public List<Question> generateQuestions(@RequestBody RequirementsDto requirementsDto) throws IOException {
        String requirements = requirementsDto.getRequirements();
        List<Question> questions = aiQuestionService.generateQuestions(requirements);

        // 打印最终返回的题目列表
        System.out.println("Returning Questions: " + questions);

        return questions;
    }
}
