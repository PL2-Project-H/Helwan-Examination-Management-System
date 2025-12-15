package lecturer;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import user.User;

import java.io.IOException;

public class LecturerDashboardController {

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
    private void handleExamManagement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ExamManagement.fxml"));
            Parent root = loader.load();
            ExamManagementController controller = loader.getController();
            controller.setUser(user);
            contentArea.getChildren().setAll(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleReporting() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Reporting.fxml"));
            Parent root = loader.load();
            ReportingController controller = loader.getController();
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
