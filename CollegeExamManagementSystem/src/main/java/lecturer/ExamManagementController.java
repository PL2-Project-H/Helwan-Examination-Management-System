package lecturer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Exam;
import user.User;
import javafx.scene.control.TextInputDialog;
import java.io.IOException;
import java.util.Optional;

public class ExamManagementController {

    @FXML
    private TableView<Exam> examsTable;

    @FXML
    private TableColumn<Exam, String> idColumn;

    @FXML
    private TableColumn<Exam, String> titleColumn;

    @FXML
    private TableColumn<Exam, String> subjectIdColumn;

    private ExamManagement examManagement;
    private User user;

    public void initialize() {
        examManagement = new ExamManagement();
        idColumn.setCellValueFactory(new PropertyValueFactory<>("examId"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        subjectIdColumn.setCellValueFactory(new PropertyValueFactory<>("subjectId"));
    }

    public void setUser(User user) {
        this.user = user;
        loadExams();
    }

    private void loadExams() {
        if (user != null) {
            ObservableList<Exam> exams = FXCollections.observableArrayList(examManagement.getExamsByLecturerId(user.getId()));
            examsTable.setItems(exams);
        }
    }

    @FXML
    private void handleAddExam() {
        TextInputDialog titleDialog = new TextInputDialog();
        titleDialog.setTitle("Add Exam");
        titleDialog.setHeaderText("Enter Exam Title");
        Optional<String> titleResult = titleDialog.showAndWait();

        if (titleResult.isPresent() && !titleResult.get().isEmpty()) {
            TextInputDialog subjectIdDialog = new TextInputDialog();
            subjectIdDialog.setTitle("Add Exam");
            subjectIdDialog.setHeaderText("Enter Subject ID");
            Optional<String> subjectIdResult = subjectIdDialog.showAndWait();

            if (subjectIdResult.isPresent() && !subjectIdResult.get().isEmpty()) {
                TextInputDialog durationDialog = new TextInputDialog();
                durationDialog.setTitle("Add Exam");
                durationDialog.setHeaderText("Enter Duration in Minutes");
                Optional<String> durationResult = durationDialog.showAndWait();

                if (durationResult.isPresent() && !durationResult.get().isEmpty()) {
                    try {
                        int duration = Integer.parseInt(durationResult.get());
                        Exam newExam = new Exam(null, subjectIdResult.get(), user.getId(), titleResult.get(), duration);
                        examManagement.addExam(newExam);
                        loadExams();
                    } catch (NumberFormatException e) {
                    
                    }
                }
            }
        }
    }

    @FXML
    private void handleEditExam() {
        Exam selectedExam = examsTable.getSelectionModel().getSelectedItem();
        if (selectedExam != null) {
            TextInputDialog titleDialog = new TextInputDialog(selectedExam.getTitle());
            titleDialog.setTitle("Edit Exam");
            titleDialog.setHeaderText("Enter New Exam Title");
            Optional<String> titleResult = titleDialog.showAndWait();

            if (titleResult.isPresent() && !titleResult.get().isEmpty()) {
                TextInputDialog subjectIdDialog = new TextInputDialog(selectedExam.getSubjectId());
                subjectIdDialog.setTitle("Edit Exam");
                subjectIdDialog.setHeaderText("Enter New Subject ID");
                Optional<String> subjectIdResult = subjectIdDialog.showAndWait();

                if (subjectIdResult.isPresent() && !subjectIdResult.get().isEmpty()) {
                    TextInputDialog durationDialog = new TextInputDialog(String.valueOf(selectedExam.getDurationInMinutes()));
                    durationDialog.setTitle("Edit Exam");
                    durationDialog.setHeaderText("Enter New Duration in Minutes");
                    Optional<String> durationResult = durationDialog.showAndWait();

                    if (durationResult.isPresent() && !durationResult.get().isEmpty()) {
                        try {
                            int duration = Integer.parseInt(durationResult.get());
                            selectedExam.setTitle(titleResult.get());
                            selectedExam.setSubjectId(subjectIdResult.get());
                            selectedExam.setDurationInMinutes(duration);
                            examManagement.updateExam(selectedExam);
                            loadExams();
                        } catch (NumberFormatException e) {
                        
                        }
                    }
                }
            }
        }
    }

    @FXML
    private void handleDeleteExam() {
        Exam selectedExam = examsTable.getSelectionModel().getSelectedItem();
        if (selectedExam != null) {
            examManagement.deleteExam(selectedExam.getExamId());
            loadExams();
        }
    }

    @FXML
    private void handleManageQuestions() {
        Exam selectedExam = examsTable.getSelectionModel().getSelectedItem();
        if (selectedExam != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/QuestionManagement.fxml"));
                Parent root = loader.load();
                QuestionManagementController controller = loader.getController();
                controller.setExam(selectedExam);
                Stage stage = new Stage();
                stage.setTitle("Manage Questions for " + selectedExam.getTitle());
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
