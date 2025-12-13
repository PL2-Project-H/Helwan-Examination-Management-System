package admin;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;

public class AddUserFormController {
    
    @FXML private TextField nameField;
    @FXML private TextField idField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<String> roleComboBox;
    
    private UserManagement userManagement;
    private Stage dialogStage;
    
    @FXML
    public void initialize() {
        roleComboBox.setItems(FXCollections.observableArrayList("STUDENT", "LECTURER", "ADMIN"));
    }
    
    public void setUserManagement(UserManagement userManagement) {
        this.userManagement = userManagement;
    }
    
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    
    @FXML
    private void handleAddUser() {
        String name = nameField.getText();
        String id = idField.getText();
        String password = passwordField.getText();
        String role = roleComboBox.getValue();
        
        if (name.isEmpty() || id.isEmpty() || password.isEmpty() || role == null) {
            showAlert("Error", "All fields are required!", Alert.AlertType.ERROR);
            return;
        }
    }
    
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }
    
    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}