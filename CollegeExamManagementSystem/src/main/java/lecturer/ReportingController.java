package lecturer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Exam;
import user.User;

public class ReportingController {

    @FXML
    private ComboBox<Exam> examComboBox;

    @FXML
    private TableView<Reporting.GradeReportEntry> gradesTable;

    @FXML
    private TableColumn<Reporting.GradeReportEntry, String> studentNameColumn;

    @FXML
    private TableColumn<Reporting.GradeReportEntry, Double> scoreColumn;

    @FXML
    private TableColumn<Reporting.GradeReportEntry, Boolean> approvedColumn;

    private Reporting reporting;
    private User user;

    public void initialize() {
        reporting = new Reporting();
        studentNameColumn.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        approvedColumn.setCellValueFactory(new PropertyValueFactory<>("approved"));
    }

    public void setUser(User user) {
        this.user = user;
        loadExams();
    }

    private void loadExams() {
        if (user != null) {
            ObservableList<Exam> exams = FXCollections.observableArrayList(reporting.getExamsByLecturer(user.getId()));
            examComboBox.setItems(exams);
        }
    }

    @FXML
    private void handleExamSelection() {
        Exam selectedExam = examComboBox.getSelectionModel().getSelectedItem();
        if (selectedExam != null) {
            ObservableList<Reporting.GradeReportEntry> grades = FXCollections.observableArrayList(reporting.getGradesForExam(selectedExam.getExamId()));
            gradesTable.setItems(grades);
        }
    }
}
