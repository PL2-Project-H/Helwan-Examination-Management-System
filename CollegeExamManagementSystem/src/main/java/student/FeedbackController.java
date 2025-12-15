package student;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import user.User;

public class FeedbackController {

    @FXML
    private TextArea feedbackTextArea;

    private Feedback feedback;
    private User user;

    public void initialize() {
        feedback = new Feedback();
    }

    public void setUser(User user) {
        this.user = user;
    }

    @FXML
    private void handleSubmitFeedback() {
        String feedbackText = feedbackTextArea.getText();
        if (user != null && feedbackText != null && !feedbackText.trim().isEmpty()) {
            feedback.submitFeedback(user.getId(), feedbackText);
            feedbackTextArea.clear();
        }
    }
}
