package student;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import user.User;

import java.io.IOException;

public class StudentDashboardController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private StackPane contentArea;

    private User user;

    public void setUser(User user) {
        this.user = user;
        welcomeLabel.setText("Welcome, " + user.getUsername() + "!");
    }

    @FXML
    private void handleExamAccess() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ExamAccess.fxml"));
            Parent root = loader.load();
            ExamAccessController controller = loader.getController();
            controller.setUser(user);
            contentArea.getChildren().setAll(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleViewResults() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ResultView.fxml"));
            Parent root = loader.load();
            ResultViewController controller = loader.getController();
            controller.setUser(user);
            contentArea.getChildren().setAll(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleReCorrection() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ReCorrectionRequest.fxml"));
            Parent root = loader.load();
            ReCorrectionController controller = loader.getController();
            controller.setUser(user);
            contentArea.getChildren().setAll(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleFeedback() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Feedback.fxml"));
            Parent root = loader.load();
            FeedbackController controller = loader.getController();
            controller.setUser(user);
            contentArea.getChildren().setAll(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUpdateProfile() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/UpdateProfile.fxml"));
            Parent root = loader.load();
            user.UpdateProfileController controller = loader.getController();
            controller.setUser(user);
            contentArea.getChildren().setAll(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
