package user;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;

public class UpdateProfileController {

    @FXML
    private Label userIdLabel;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField currentPasswordField;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private PasswordField confirmPasswordField;

    private User user;

    public void setUser(User user) {
        this.user = user;
        userIdLabel.setText("User ID: " + user.getId() + " (cannot be changed)");
        usernameField.setText(user.getUsername());
    }

    @FXML
    private void handleUpdateUsername() {
        if (user == null) return;

        String newUsername = usernameField.getText().trim();
        if (newUsername.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Username cannot be empty.");
            return;
        }

        if (newUsername.equals(user.getUsername())) {
            showAlert(Alert.AlertType.INFORMATION, "No Change", "Username is the same as current.");
            return;
        }

        boolean success = UpdateProfile.updateUsername(user.getId(), newUsername);
        if (success) {
            user.setUsername(newUsername);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Username updated successfully!");
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", 
                "Failed to update username. Username must be at least 3 characters.");
        }
    }

    @FXML
    private void handleUpdatePassword() {
        if (user == null) return;

        String currentPassword = currentPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "All password fields are required.");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Error", "New password and confirmation do not match.");
            return;
        }

        if (newPassword.equals(currentPassword)) {
            showAlert(Alert.AlertType.INFORMATION, "No Change", 
                "New password is the same as current password.");
            return;
        }

        boolean success = UpdateProfile.updatePassword(user.getId(), currentPassword, newPassword);
        if (success) {
            user.setPassword(newPassword);
            currentPasswordField.clear();
            newPasswordField.clear();
            confirmPasswordField.clear();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Password updated successfully!");
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", 
                "Failed to update password. Check current password and ensure new password is at least 6 characters.");
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
