package admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import student.ReCorrectionRequest;
import user.User;
import model.Grade;
import model.Exam;
import lecturer.ExamManagement;

import java.util.List;
import java.util.Optional;

public class ReCorrectionReviewController {

    @FXML
    private TableView<RequestEntry> requestsTable;

    @FXML
    private TableColumn<RequestEntry, String> requestIdColumn;

    @FXML
    private TableColumn<RequestEntry, String> studentNameColumn;

    @FXML
    private TableColumn<RequestEntry, String> examTitleColumn;

    @FXML
    private TableColumn<RequestEntry, Double> scoreColumn;

    @FXML
    private TableColumn<RequestEntry, String> statusColumn;

    @FXML
    private TableColumn<RequestEntry, String> timestampColumn;

    @FXML
    private TextArea reasonTextArea;

    @FXML
    private Label infoLabel;

    private GradeApproval gradeApproval;
    private UserManagement userManagement;
    private ExamManagement examManagement;

    public void initialize() {
        gradeApproval = new GradeApproval();
        userManagement = new UserManagement();
        examManagement = new ExamManagement();
        
        requestIdColumn.setCellValueFactory(new PropertyValueFactory<>("requestId"));
        studentNameColumn.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        examTitleColumn.setCellValueFactory(new PropertyValueFactory<>("examTitle"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        timestampColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));

        loadRequests();

        // Show reason when request is selected
        requestsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                reasonTextArea.setText("Reason: " + newSelection.getReason());
            }
        });
    }

    private void loadRequests() {
        List<ReCorrectionRequest> allRequests = ReCorrectionRequest.getAllRequests();
        
        ObservableList<RequestEntry> entries = FXCollections.observableArrayList();
        for (ReCorrectionRequest req : allRequests) {
            // Find student, exam, and grade details
            User student = userManagement.getAllUsers().stream()
                    .filter(u -> u.getId().equals(req.getStudentId()))
                    .findFirst()
                    .orElse(null);

            Exam exam = examManagement.getAllExams().stream()
                    .filter(e -> e.getExamId().equals(req.getExamId()))
                    .findFirst()
                    .orElse(null);

            Grade grade = gradeApproval.getAllGrades().stream()
                    .filter(g -> g.getGradeId().equals(req.getGradeId()))
                    .findFirst()
                    .orElse(null);

            String studentName = student != null ? student.getUsername() : "Unknown";
            String examTitle = exam != null ? exam.getTitle() : "Unknown";
            double score = grade != null ? grade.getScore() : 0.0;

            entries.add(new RequestEntry(
                req.getRequestId(),
                studentName,
                examTitle,
                score,
                req.getStatus().name(),
                req.getTimestamp(),
                req.getReason(),
                req.getGradeId()
            ));
        }

        requestsTable.setItems(entries);
        infoLabel.setText(String.format("Total requests: %d", entries.size()));
    }

    @FXML
    private void handleApprove() {
        RequestEntry selected = requestsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a request to approve.");
            return;
        }

        if (!selected.getStatus().equals("PENDING")) {
            showAlert(Alert.AlertType.WARNING, "Invalid Status", 
                "Only pending requests can be approved.");
            return;
        }

        // Get admin comment
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Approve Request");
        dialog.setHeaderText("Provide a comment (optional)");
        dialog.setContentText("Comment:");

        Optional<String> result = dialog.showAndWait();
        String comment = result.orElse("");

        ReCorrectionRequest.updateRequest(
            selected.getRequestId(),
            ReCorrectionRequest.RequestStatus.APPROVED,
            comment
        );

        showAlert(Alert.AlertType.INFORMATION, "Success", "Request approved successfully.");
        loadRequests();
    }

    @FXML
    private void handleReject() {
        RequestEntry selected = requestsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a request to reject.");
            return;
        }

        if (!selected.getStatus().equals("PENDING")) {
            showAlert(Alert.AlertType.WARNING, "Invalid Status", 
                "Only pending requests can be rejected.");
            return;
        }

        // Get admin comment
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Reject Request");
        dialog.setHeaderText("Provide a reason for rejection");
        dialog.setContentText("Reason:");

        Optional<String> result = dialog.showAndWait();
        if (!result.isPresent() || result.get().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please provide a reason for rejection.");
            return;
        }

        ReCorrectionRequest.updateRequest(
            selected.getRequestId(),
            ReCorrectionRequest.RequestStatus.REJECTED,
            result.get().trim()
        );

        showAlert(Alert.AlertType.INFORMATION, "Success", "Request rejected.");
        loadRequests();
    }

    @FXML
    private void handleRefresh() {
        loadRequests();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static class RequestEntry {
        private String requestId;
        private String studentName;
        private String examTitle;
        private Double score;
        private String status;
        private String timestamp;
        private String reason;
        private String gradeId;

        public RequestEntry(String requestId, String studentName, String examTitle, Double score,
                             String status, String timestamp, String reason, String gradeId) {
            this.requestId = requestId;
            this.studentName = studentName;
            this.examTitle = examTitle;
            this.score = score;
            this.status = status;
            this.timestamp = timestamp;
            this.reason = reason;
            this.gradeId = gradeId;
        }

        public String getRequestId() { return requestId; }
        public String getStudentName() { return studentName; }
        public String getExamTitle() { return examTitle; }
        public Double getScore() { return score; }
        public String getStatus() { return status; }
        public String getTimestamp() { return timestamp; }
        public String getReason() { return reason; }
        public String getGradeId() { return gradeId; }
    }
}
