package student;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Toggle;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.util.Duration;
import model.Exam;
import model.MultipleChoiceQuestion;
import model.Question;
import model.TrueFalseQuestion;
import model.ShortAnswerQuestion;
import user.User;
import lecturer.AutomaticGrading;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

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
    private List<Question> questions;
    private Map<String, VBox> questionBoxMap;

    public void initialize() {
        examAccess = new ExamAccess();
        questionBoxMap = new HashMap<>();
    }

    public void setExam(Exam exam, User user) {
        try {
            if (exam == null || user == null) {
                showError("Invalid Data", "Exam or user information is missing.");
                return;
            }
            
            this.exam = exam;
            this.user = user;
            examTitleLabel.setText(exam.getTitle());
            timeSeconds = exam.getDurationInMinutes() * 60;
            startTimer();
            loadQuestions();
        } catch (Exception e) {
            showError("Error Loading Exam", "Failed to load exam: " + e.getMessage());
            e.printStackTrace();
        }
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
        try {
            questions = examAccess.getQuestionsForExam(exam.getExamId());
            
            if (questions == null || questions.isEmpty()) {
                showError("No Questions", "This exam has no questions.");
                return;
            }
            
            for (Question question : questions) {
                VBox questionBox = new VBox(5);
                Label questionText = new Label(question.getQuestionText());
                questionBox.getChildren().add(questionText);

                if (question instanceof MultipleChoiceQuestion) {
                    MultipleChoiceQuestion mcq = (MultipleChoiceQuestion) question;
                    ToggleGroup group = new ToggleGroup();
                    int index = 0;
                    for (String option : mcq.getOptions()) {
                        RadioButton rb = new RadioButton(option);
                        rb.setToggleGroup(group);
                        rb.setUserData(String.valueOf(index)); // Store index as user data
                        questionBox.getChildren().add(rb);
                        index++;
                    }
                } else if (question instanceof TrueFalseQuestion) {
                    ToggleGroup group = new ToggleGroup();
                    RadioButton trueRb = new RadioButton("True");
                    trueRb.setToggleGroup(group);
                    trueRb.setUserData("true");
                    RadioButton falseRb = new RadioButton("False");
                    falseRb.setToggleGroup(group);
                    falseRb.setUserData("false");
                    questionBox.getChildren().addAll(trueRb, falseRb);
                } else { // ShortAnswerQuestion
                    TextArea answerArea = new TextArea();
                    answerArea.setPromptText("Enter your answer here...");
                    answerArea.setPrefRowCount(3);
                    questionBox.getChildren().add(answerArea);
                }
                
                // Store the questionBox in the map for later retrieval
                questionBoxMap.put(question.getQuestionId(), questionBox);
                questionsVBox.getChildren().add(questionBox);
            }
        } catch (Exception e) {
            showError("Error Loading Questions", "Failed to load questions: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSubmitExam() {
        try {
            // Stop timer
            if (timeline != null) {
                timeline.stop();
            }

            // Collect answers from UI
            Map<String, String> answers = collectAnswers();
            
            // Save answers
            examAccess.saveAnswers(user.getId(), exam.getExamId(), answers);
            
            // Record attempt
            examAccess.recordAttempt(user.getId(), exam.getExamId());
            
            // Calculate and save grade automatically
            AutomaticGrading grading = new AutomaticGrading();
            double score = grading.calculateAndSaveGrade(user.getId(), exam.getExamId(), answers);
            
            // Show confirmation
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Exam Submitted");
            alert.setHeaderText("Exam submitted successfully!");
            alert.setContentText(String.format("Your exam has been submitted.\nScore: %.2f%%\n\nYour grade is pending approval by the administrator.", score));
            alert.showAndWait();
            
            // Close the window
            timerLabel.getScene().getWindow().hide();
        } catch (Exception e) {
            showError("Submission Error", "Failed to submit exam: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Collects answers from the UI controls
     */
    private Map<String, String> collectAnswers() {
        Map<String, String> answers = new HashMap<>();
        
        if (questions == null) {
            return answers;
        }
        
        for (Question question : questions) {
            VBox questionBox = questionBoxMap.get(question.getQuestionId());
            if (questionBox == null) {
                continue;
            }
            
            String answer = null;
            
            if (question instanceof MultipleChoiceQuestion || question instanceof TrueFalseQuestion) {
                // Find the toggle group and get selected value
                for (Node node : questionBox.getChildren()) {
                    if (node instanceof RadioButton) {
                        RadioButton rb = (RadioButton) node;
                        if (rb.isSelected() && rb.getUserData() != null) {
                            answer = rb.getUserData().toString();
                            break;
                        }
                    }
                }
            } else if (question instanceof ShortAnswerQuestion) {
                // Find the text area
                for (Node node : questionBox.getChildren()) {
                    if (node instanceof TextArea) {
                        TextArea textArea = (TextArea) node;
                        answer = textArea.getText();
                        break;
                    }
                }
            }
            
            if (answer != null && !answer.trim().isEmpty()) {
                answers.put(question.getQuestionId(), answer.trim());
            }
        }
        
        return answers;
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
