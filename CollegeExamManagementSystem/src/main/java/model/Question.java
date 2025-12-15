package model;

public abstract class Question {
    private String questionId;
    private String examId;
    private String questionText;

    public Question(String questionId, String examId, String questionText) {
        this.questionId = questionId;
        this.examId = examId;
        this.questionText = questionText;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public abstract String getType();
}
