package student;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import model.Grade;
import model.Exam;
import model.Subject;
import user.User;
import lecturer.ExamManagement;
import admin.SubjectManagement;
import admin.GradeApproval;
import java.util.List;
import java.util.stream.Collectors;

public class ResultViewController {

    @FXML
    private TableView<ResultEntry> resultsTable;

    @FXML
    private TableColumn<ResultEntry, String> examTitleColumn;

    @FXML
    private TableColumn<ResultEntry, String> subjectColumn;

    @FXML
    private TableColumn<ResultEntry, Double> scoreColumn;

    @FXML
    private TableColumn<ResultEntry, String> statusColumn;

    @FXML
    private Label infoLabel;

    private User user;
    private GradeApproval gradeApproval;
    private ExamManagement examManagement;
    private SubjectManagement subjectManagement;

    public void initialize() {
        gradeApproval = new GradeApproval();
        examManagement = new ExamManagement();
        subjectManagement = new SubjectManagement();
        
        examTitleColumn.setCellValueFactory(new PropertyValueFactory<>("examTitle"));
        subjectColumn.setCellValueFactory(new PropertyValueFactory<>("subjectName"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    public void setUser(User user) {
        this.user = user;
        loadResults();
    }

    private void loadResults() {
        if (user == null) return;

        // Get all grades for this student
        List<Grade> studentGrades = gradeApproval.getAllGrades().stream()
                .filter(g -> g.getStudentId().equals(user.getId()))
                .filter(Grade::isApproved) // Only show approved grades
                .collect(Collectors.toList());

        if (studentGrades.isEmpty()) {
            infoLabel.setText("No published results yet.");
            return;
        }

        // Convert to display entries
        ObservableList<ResultEntry> results = FXCollections.observableArrayList();
        for (Grade grade : studentGrades) {
            // Find exam details
            Exam exam = examManagement.getAllExams().stream()
                    .filter(e -> e.getExamId().equals(grade.getExamId()))
                    .findFirst()
                    .orElse(null);

            if (exam != null) {
                // Find subject name
                Subject subject = subjectManagement.getAllSubjects().stream()
                        .filter(s -> s.getSubjectId().equals(exam.getSubjectId()))
                        .findFirst()
                        .orElse(null);

                String examTitle = exam.getTitle();
                String subjectName = subject != null ? subject.getSubjectName() : "Unknown";
                String status = grade.isApproved() ? "Published" : "Pending";

                results.add(new ResultEntry(examTitle, subjectName, grade.getScore(), status));
            }
        }

        resultsTable.setItems(results);
        infoLabel.setText(String.format("You have %d published result(s).", results.size()));
    }

    /**
     * Inner class to represent a result entry for display
     */
    public static class ResultEntry {
        private String examTitle;
        private String subjectName;
        private Double score;
        private String status;

        public ResultEntry(String examTitle, String subjectName, Double score, String status) {
            this.examTitle = examTitle;
            this.subjectName = subjectName;
            this.score = score;
            this.status = status;
        }

        public String getExamTitle() { return examTitle; }
        public String getSubjectName() { return subjectName; }
        public Double getScore() { return score; }
        public String getStatus() { return status; }
    }
}
