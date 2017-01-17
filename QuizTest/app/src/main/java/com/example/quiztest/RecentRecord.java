package com.example.quiztest;

public class RecentRecord {
    private int id;
    private String difficulty;
    private String date;
    private String questionNumber;
    private String TimeNumber;
    private String correctPercent;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(String questionNumber) {
        this.questionNumber = questionNumber;
    }

    public String getTimeNumber() {
        return TimeNumber;
    }

    public void setTimeNumber(String timeNumber) {
        TimeNumber = timeNumber;
    }

    public String getCorrectPercent() {
        return correctPercent;
    }

    public void setCorrectPercent(String correctPercent) {
        this.correctPercent = correctPercent;
    }
}
