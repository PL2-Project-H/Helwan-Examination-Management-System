package admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import student.Feedback;
import user.User;

import java.util.List;

public class FeedbackViewController {

    @FXML
    private TableView<FeedbackEntry> feedbackTable;

    @FXML
    private TableColumn<FeedbackEntry, String> studentNameColumn;

    @FXML
    private TableColumn<FeedbackEntry, String> timestampColumn;

    @FXML
    private TextArea feedbackTextArea;

    @FXML
    private Label infoLabel;

    private Feedback feedbackService;
    private UserManagement userManagement;

    public void initialize() {
        feedbackService = new Feedback();
        userManagement = new UserManagement();
        
        studentNameColumn.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        timestampColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));

        loadFeedback();

        // Show full feedback text when selected
        feedbackTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                feedbackTextArea.setText(newSelection.getFeedbackText());
            }
        });
    }

    private void loadFeedback() {
        List<Feedback.FeedbackEntry> allFeedback = feedbackService.getAllFeedback();
        
        ObservableList<FeedbackEntry> entries = FXCollections.observableArrayList();
        for (Feedback.FeedbackEntry fb : allFeedback) {
            // Find student name
            User student = userManagement.getAllUsers().stream()
                    .filter(u -> u.getId().equals(fb.getStudentId()))
                    .findFirst()
                    .orElse(null);

            String studentName = student != null ? student.getUsername() : "Unknown (ID: " + fb.getStudentId() + ")";

            entries.add(new FeedbackEntry(
                studentName,
                fb.getTimestamp(),
                fb.getFeedbackText()
            ));
        }

        feedbackTable.setItems(entries);
        infoLabel.setText(String.format("Total feedback submissions: %d", entries.size()));
    }

    @FXML
    private void handleRefresh() {
        loadFeedback();
    }

    public static class FeedbackEntry {
        private String studentName;
        private String timestamp;
        private String feedbackText;

        public FeedbackEntry(String studentName, String timestamp, String feedbackText) {
            this.studentName = studentName;
            this.timestamp = timestamp;
            this.feedbackText = feedbackText;
        }

        public String getStudentName() { return studentName; }
        public String getTimestamp() { return timestamp; }
        public String getFeedbackText() { return feedbackText; }
    }
}
