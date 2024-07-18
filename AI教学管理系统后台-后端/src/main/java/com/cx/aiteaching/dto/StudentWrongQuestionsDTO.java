package com.cx.aiteaching.dto;

public class StudentWrongQuestionsDTO {
    private String question;
    private String studentAnswer;
    private String correctAnswer;
    private String analysis;

    // Getter and Setter for question
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    // Getter and Setter for studentAnswer
    public String getStudentAnswer() {
        return studentAnswer;
    }

    public void setStudentAnswer(String studentAnswer) {
        this.studentAnswer = studentAnswer;
    }

    // Getter and Setter for correctAnswer
    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    // Getter and Setter for analysis
    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }
}
