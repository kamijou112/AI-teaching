package com.cx.aiteaching.service.impl;

import com.cx.aiteaching.dto.StudentWrongQuestionsDTO;
import com.cx.aiteaching.mapper.QuestionMapper;
import com.cx.aiteaching.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Override
    public List<StudentWrongQuestionsDTO> getStudentWrongQuestions(int studentId) {
        return questionMapper.getStudentWrongQuestions(studentId);
    }
}
