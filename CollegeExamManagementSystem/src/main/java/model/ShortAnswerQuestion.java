package model;

public class ShortAnswerQuestion extends Question {
    private String correctAnswer;

    public ShortAnswerQuestion(String questionId, String examId, String questionText, String correctAnswer) {
        super(questionId, examId, questionText);
        this.correctAnswer = correctAnswer;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    @Override
    public String getType() {
        return "ShortAnswer";
    }
}
