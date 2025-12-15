package lecturer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Exam;
import model.Question;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ChoiceDialog;
import java.util.Optional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import model.MultipleChoiceQuestion;
import model.ShortAnswerQuestion;
import model.TrueFalseQuestion;

public class QuestionManagementController {

    @FXML
    private TableView<Question> questionsTable;

    @FXML
    private TableColumn<Question, String> idColumn;

    @FXML
    private TableColumn<Question, String> textColumn;

    @FXML
    private TableColumn<Question, String> typeColumn;

    private ExamManagement examManagement;
    private Exam exam;

    public void initialize() {
        examManagement = new ExamManagement();
        idColumn.setCellValueFactory(new PropertyValueFactory<>("questionId"));
        textColumn.setCellValueFactory(new PropertyValueFactory<>("questionText"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
    }

    public void setExam(Exam exam) {
        this.exam = exam;
        loadQuestions();
    }

    private void loadQuestions() {
        if (exam != null) {
            ObservableList<Question> questions = FXCollections.observableArrayList(examManagement.getQuestionsByExamId(exam.getExamId()));
            questionsTable.setItems(questions);
        }
    }

    @FXML
    private void handleAddQuestion() {
        List<String> questionTypes = Arrays.asList("MultipleChoice", "TrueFalse", "ShortAnswer");
        ChoiceDialog<String> typeDialog = new ChoiceDialog<>("MultipleChoice", questionTypes);
        typeDialog.setTitle("Add Question");
        typeDialog.setHeaderText("Select Question Type");
        Optional<String> typeResult = typeDialog.showAndWait();

        if (typeResult.isPresent()) {
            TextInputDialog textDialog = new TextInputDialog();
            textDialog.setTitle("Add Question");
            textDialog.setHeaderText("Enter Question Text");
            Optional<String> textResult = textDialog.showAndWait();

            if (textResult.isPresent() && !textResult.get().isEmpty()) {
                String questionText = textResult.get();
                Question newQuestion = null;
                switch (typeResult.get()) {
                    case "MultipleChoice":
                        TextInputDialog optionsDialog = new TextInputDialog();
                        optionsDialog.setTitle("Add Multiple Choice Question");
                        optionsDialog.setHeaderText("Enter options (separated by ';')");
                        Optional<String> optionsResult = optionsDialog.showAndWait();
                        if (optionsResult.isPresent() && !optionsResult.get().isEmpty()) {
                            List<String> options = Arrays.asList(optionsResult.get().split(";"));
                            TextInputDialog correctOptionDialog = new TextInputDialog();
                            correctOptionDialog.setTitle("Add Multiple Choice Question");
                            correctOptionDialog.setHeaderText("Enter correct option index (0-based)");
                            Optional<String> correctOptionResult = correctOptionDialog.showAndWait();
                            if (correctOptionResult.isPresent() && !correctOptionResult.get().isEmpty()) {
                                try {
                                    int correctOption = Integer.parseInt(correctOptionResult.get());
                                    newQuestion = new MultipleChoiceQuestion(null, exam.getExamId(), questionText, options, correctOption);
                                } catch (NumberFormatException e) {
                                    // handle error
                                }
                            }
                        }
                        break;
                    case "TrueFalse":
                        ChoiceDialog<Boolean> answerDialog = new ChoiceDialog<>(true, Arrays.asList(true, false));
                        answerDialog.setTitle("Add True/False Question");
                        answerDialog.setHeaderText("Select the correct answer");
                        Optional<Boolean> answerResult = answerDialog.showAndWait();
                        if (answerResult.isPresent()) {
                            newQuestion = new TrueFalseQuestion(null, exam.getExamId(), questionText, answerResult.get());
                        }
                        break;
                    case "ShortAnswer":
                        TextInputDialog correctAnswerDialog = new TextInputDialog();
                        correctAnswerDialog.setTitle("Add Short Answer Question");
                        correctAnswerDialog.setHeaderText("Enter the correct answer");
                        Optional<String> correctAnswerResult = correctAnswerDialog.showAndWait();
                        if (correctAnswerResult.isPresent() && !correctAnswerResult.get().isEmpty()) {
                            newQuestion = new ShortAnswerQuestion(null, exam.getExamId(), questionText, correctAnswerResult.get());
                        }
                        break;
                }
                if (newQuestion != null) {
                    examManagement.addQuestion(newQuestion);
                    loadQuestions();
                }
            }
        }
    }

    @FXML
    private void handleEditQuestion() {
        Question selectedQuestion = questionsTable.getSelectionModel().getSelectedItem();
        if (selectedQuestion != null) {
            TextInputDialog textDialog = new TextInputDialog(selectedQuestion.getQuestionText());
            textDialog.setTitle("Edit Question");
            textDialog.setHeaderText("Enter Question Text");
            Optional<String> textResult = textDialog.showAndWait();

            if (textResult.isPresent() && !textResult.get().isEmpty()) {
                selectedQuestion.setQuestionText(textResult.get());
                if (selectedQuestion instanceof MultipleChoiceQuestion) {
                    // ... implementation for editing multiple choice questions
                } else if (selectedQuestion instanceof TrueFalseQuestion) {
                    // ... implementation for editing true/false questions
                } else if (selectedQuestion instanceof ShortAnswerQuestion) {
                    // ... implementation for editing short answer questions
                }
                examManagement.updateQuestion(selectedQuestion);
                loadQuestions();
            }
        }
    }

    @FXML
    private void handleDeleteQuestion() {
        Question selectedQuestion = questionsTable.getSelectionModel().getSelectedItem();
        if (selectedQuestion != null) {
            examManagement.deleteQuestion(selectedQuestion.getQuestionId());
            loadQuestions();
        }
    }
}
