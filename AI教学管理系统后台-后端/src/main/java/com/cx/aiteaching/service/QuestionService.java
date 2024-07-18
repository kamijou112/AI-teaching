package com.cx.aiteaching.service;

import com.cx.aiteaching.dto.StudentWrongQuestionsDTO;
import java.util.List;

public interface QuestionService {
    List<StudentWrongQuestionsDTO> getStudentWrongQuestions(int studentId);
}
