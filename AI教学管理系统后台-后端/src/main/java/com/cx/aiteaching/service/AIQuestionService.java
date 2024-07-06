package com.cx.aiteaching.service;

import com.cx.aiteaching.entity.Question;

import java.io.IOException;
import java.util.List;

public interface AIQuestionService {
    List<Question> generateQuestions(String requirements) throws IOException;
}
