package student;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import user.User;
import model.Grade;
import model.Exam;
import admin.GradeApproval;
import lecturer.ExamManagement;

import java.util.List;
import java.util.Optional;

public class ReCorrectionController {

    @FXML
    private TableView<ReCorrectionEntry> requestsTable;

    @FXML
    private TableColumn<ReCorrectionEntry, String> examTitleColumn;

    @FXML
    private TableColumn<ReCorrectionEntry, Double> scoreColumn;

    @FXML
    private TableColumn<ReCorrectionEntry, String> statusColumn;

    @FXML
    private TableColumn<ReCorrectionEntry, String> timestampColumn;

    @FXML
    private Button submitRequestButton;

    @FXML
    private Label infoLabel;

    private User user;
    private GradeApproval gradeApproval;
    private ExamManagement examManagement;

    public void initialize() {
        gradeApproval = new GradeApproval();
        examManagement = new ExamManagement();
        
        examTitleColumn.setCellValueFactory(new PropertyValueFactory<>("examTitle"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        timestampColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
    }

    public void setUser(User user) {
        this.user = user;
        loadRequests();
    }

    private void loadRequests() {
        if (user == null) return;

        List<ReCorrectionRequest> requests = ReCorrectionRequest.getRequestsByStudent(user.getId());
        
        ObservableList<ReCorrectionEntry> entries = FXCollections.observableArrayList();
        for (ReCorrectionRequest req : requests) {
            // Find exam and grade details
            Exam exam = examManagement.getAllExams().stream()
                    .filter(e -> e.getExamId().equals(req.getExamId()))
                    .findFirst()
                    .orElse(null);

            Grade grade = gradeApproval.getAllGrades().stream()
                    .filter(g -> g.getGradeId().equals(req.getGradeId()))
                    .findFirst()
                    .orElse(null);

            String examTitle = exam != null ? exam.getTitle() : "Unknown";
            double score = grade != null ? grade.getScore() : 0.0;

            entries.add(new ReCorrectionEntry(
                req.getRequestId(),
                examTitle,
                score,
                req.getStatus().name(),
                req.getTimestamp(),
                req.getGradeId()
            ));
        }

        requestsTable.setItems(entries);
        infoLabel.setText(String.format("You have %d re-correction request(s).", entries.size()));
    }

    @FXML
    private void handleSubmitRequest() {
        if (user == null) return;

        // Get published grades for student
        List<Grade> publishedGrades = gradeApproval.getAllGrades().stream()
                .filter(g -> g.getStudentId().equals(user.getId()))
                .filter(Grade::isApproved)
                .toList();

        if (publishedGrades.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "No Results", 
                "You don't have any published results to request re-correction for.");
            return;
        }

        // Let student select which grade to request re-correction for
        ChoiceDialog<Grade> gradeDialog = new ChoiceDialog<>(publishedGrades.get(0), publishedGrades);
        gradeDialog.setTitle("Request Re-Correction");
        gradeDialog.setHeaderText("Select exam result");
        gradeDialog.setContentText("Choose the exam:");

        Optional<Grade> gradeResult = gradeDialog.showAndWait();
        if (!gradeResult.isPresent()) return;

        Grade selectedGrade = gradeResult.get();

        // Check if already requested
        List<ReCorrectionRequest> existing = ReCorrectionRequest.getRequestsByStudent(user.getId()).stream()
                .filter(r -> r.getGradeId().equals(selectedGrade.getGradeId()))
                .filter(r -> r.getStatus() == ReCorrectionRequest.RequestStatus.PENDING)
                .toList();

        if (!existing.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Request Exists", 
                "You already have a pending re-correction request for this exam.");
            return;
        }

        // Get reason
        TextInputDialog reasonDialog = new TextInputDialog();
        reasonDialog.setTitle("Request Re-Correction");
        reasonDialog.setHeaderText("Provide a reason for re-correction");
        reasonDialog.setContentText("Reason:");
        reasonDialog.getEditor().setPrefColumnCount(30);

        Optional<String> reasonResult = reasonDialog.showAndWait();
        if (!reasonResult.isPresent() || reasonResult.get().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please provide a reason for the request.");
            return;
        }

        // Submit request
        String requestId = ReCorrectionRequest.submitRequest(
            user.getId(),
            selectedGrade.getExamId(),
            selectedGrade.getGradeId(),
            reasonResult.get().trim()
        );

        showAlert(Alert.AlertType.INFORMATION, "Success", 
            "Your re-correction request has been submitted successfully.\nRequest ID: " + requestId);

        loadRequests();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static class ReCorrectionEntry {
        private String requestId;
        private String examTitle;
        private Double score;
        private String status;
        private String timestamp;
        private String gradeId;

        public ReCorrectionEntry(String requestId, String examTitle, Double score, 
                                  String status, String timestamp, String gradeId) {
            this.requestId = requestId;
            this.examTitle = examTitle;
            this.score = score;
            this.status = status;
            this.timestamp = timestamp;
            this.gradeId = gradeId;
        }

        public String getRequestId() { return requestId; }
        public String getExamTitle() { return examTitle; }
        public Double getScore() { return score; }
        public String getStatus() { return status; }
        public String getTimestamp() { return timestamp; }
        public String getGradeId() { return gradeId; }
    }
}
