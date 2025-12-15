package student;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import model.Exam;
import model.MultipleChoiceQuestion;
import model.Question;
import model.TrueFalseQuestion;
import user.User;

import java.util.HashMap;
import java.util.Map;

public class ExamViewController {

    @FXML
    private Label examTitleLabel;

    @FXML
    private Label timerLabel;

    @FXML
    private VBox questionsVBox;

    private Exam exam;
    private User user;
    private ExamAccess examAccess;
    private int timeSeconds;
    private Timeline timeline;

    public void initialize() {
        examAccess = new ExamAccess();
    }

    public void setExam(Exam exam, User user) {
        this.exam = exam;
        this.user = user;
        examTitleLabel.setText(exam.getTitle());
        timeSeconds = exam.getDurationInMinutes() * 60;
        startTimer();
        loadQuestions();
    }

    private void startTimer() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            timeSeconds--;
            int minutes = timeSeconds / 60;
            int seconds = timeSeconds % 60;
            timerLabel.setText(String.format("Time remaining: %02d:%02d", minutes, seconds));
            if (timeSeconds <= 0) {
                timeline.stop();
                handleSubmitExam();
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void loadQuestions() {
        for (Question question : examAccess.getQuestionsForExam(exam.getExamId())) {
            VBox questionBox = new VBox(5);
            Label questionText = new Label(question.getQuestionText());
            questionBox.getChildren().add(questionText);

            if (question instanceof MultipleChoiceQuestion) {
                ToggleGroup group = new ToggleGroup();
                for (String option : ((MultipleChoiceQuestion) question).getOptions()) {
                    RadioButton rb = new RadioButton(option);
                    rb.setToggleGroup(group);
                    questionBox.getChildren().add(rb);
                }
            } else if (question instanceof TrueFalseQuestion) {
                ToggleGroup group = new ToggleGroup();
                RadioButton trueRb = new RadioButton("True");
                trueRb.setToggleGroup(group);
                RadioButton falseRb = new RadioButton("False");
                falseRb.setToggleGroup(group);
                questionBox.getChildren().addAll(trueRb, falseRb);
            } else { // ShortAnswerQuestion
                questionBox.getChildren().add(new TextArea());
            }
            questionsVBox.getChildren().add(questionBox);
        }
    }

    @FXML
    private void handleSubmitExam() {
        Map<String, String> answers = new HashMap<>();
        // Logic to get answers from the UI controls
        examAccess.saveAnswers(user.getId(), exam.getExamId(), answers);
        examAccess.recordAttempt(user.getId(), exam.getExamId());
        // Close the window
        timerLabel.getScene().getWindow().hide();
    }
}
