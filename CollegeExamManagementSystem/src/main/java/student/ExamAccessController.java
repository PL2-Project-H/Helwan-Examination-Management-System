package student;

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

import java.io.IOException;

public class ExamAccessController {

    @FXML
    private TableView<Exam> examsTable;

    @FXML
    private TableColumn<Exam, String> titleColumn;

    @FXML
    private TableColumn<Exam, String> subjectColumn;

    @FXML
    private TableColumn<Exam, Integer> durationColumn;

    private ExamAccess examAccess;
    private User user;

    public void initialize() {
        examAccess = new ExamAccess();
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        subjectColumn.setCellValueFactory(new PropertyValueFactory<>("subjectId"));
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("durationInMinutes"));
    }

    public void setUser(User user) {
        this.user = user;
        loadExams();
    }

    private void loadExams() {
        if (user != null) {
            ObservableList<Exam> exams = FXCollections.observableArrayList(examAccess.getAvailableExamsForStudent(user.getId()));
            examsTable.setItems(exams);
        }
    }

    @FXML
    private void handleStartExam() {
        Exam selectedExam = examsTable.getSelectionModel().getSelectedItem();
        if (selectedExam != null) {
            if (examAccess.hasAttemptedExam(user.getId(), selectedExam.getExamId())) {
                // Show an alert that the exam has already been attempted
                return;
            }
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ExamView.fxml"));
                Parent root = loader.load();
                ExamViewController controller = loader.getController();
                controller.setExam(selectedExam, user);
                Stage stage = new Stage();
                stage.setTitle(selectedExam.getTitle());
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
