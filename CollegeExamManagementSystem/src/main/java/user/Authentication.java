package user;

import admin.AdminDashboardController;
import lecturer.LecturerDashboardController;
import student.StudentDashboardController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import util.FileHandler;
import util.Validator;

public class Authentication {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (!Validator.isValidUsername(username) || !Validator.isValidPassword(password)) {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password format.");
            return;
        }

        User authenticatedUser = authenticate(username, password);

        if (authenticatedUser != null) {
            loadDashboard(authenticatedUser);
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Incorrect username or password.");
        }
    }

    private User authenticate(String username, String password) {
        List<String> userLines = FileHandler.readAllLines("users.txt");
        for (String line : userLines) {
            String[] parts = line.split(",");
            if (parts.length == 4) {
                String id = parts[0];
                String storedUsername = parts[1];
                String storedPassword = parts[2];
                User.Role role = User.Role.valueOf(parts[3]);

                if (storedUsername.equals(username) && storedPassword.equals(password)) {
                    return new User(id, storedUsername, storedPassword, role);
                }
            }
        }
        return null;
    }

    private void loadDashboard(User user) {
        try {
            String fxmlPath = "";
            String title = "";
            switch (user.getRole()) {
                case ADMIN:
                    fxmlPath = "/fxml/AdminDashboard.fxml";
                    title = "Admin Dashboard";
                    break;
                case LECTURER:
                    fxmlPath = "/fxml/LecturerDashboard.fxml";
                    title = "Lecturer Dashboard";
                    break;
                case STUDENT:
                    fxmlPath = "/fxml/StudentDashboard.fxml";
                    title = "Student Dashboard";
                    break;
            }

            URL fxmlUrl = getClass().getResource(fxmlPath);
            if (fxmlUrl == null) {
                System.err.println("Cannot find " + fxmlPath);
                return;
            }
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent dashboardRoot = loader.load();

            Object controller = loader.getController();
            if (controller instanceof AdminDashboardController) {
                ((AdminDashboardController) controller).setUser(user);
            } else if (controller instanceof LecturerDashboardController) {
                ((LecturerDashboardController) controller).setUser(user);
            } else if (controller instanceof StudentDashboardController) {
                ((StudentDashboardController) controller).setUser(user);
            }


            Stage stage = (Stage) loginButton.getScene().getWindow();
            Scene scene = new Scene(dashboardRoot, 1000, 700); 
            stage.setTitle(title);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Could not load dashboard.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
