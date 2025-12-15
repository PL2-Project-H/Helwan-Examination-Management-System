package model;

import java.util.List;

public class MultipleChoiceQuestion extends Question {
    private List<String> options;
    private int correctOptionIndex;

    public MultipleChoiceQuestion(String questionId, String examId, String questionText, List<String> options, int correctOptionIndex) {
        super(questionId, examId, questionText);
        this.options = options;
        this.correctOptionIndex = correctOptionIndex;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public int getCorrectOptionIndex() {
        return correctOptionIndex;
    }

    public void setCorrectOptionIndex(int correctOptionIndex) {
        this.correctOptionIndex = correctOptionIndex;
    }

    @Override
    public String getType() {
        return "MultipleChoice";
    }
}
