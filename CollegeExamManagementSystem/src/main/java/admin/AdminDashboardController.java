package admin;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;

public class AdminDashboardController {
    
    // Link to FXML elements using fx:id
    @FXML private Button userManagementBtn;
    @FXML private Button subjectManagementBtn;
    @FXML private Button gradeApprovalBtn;
    @FXML private Button logoutBtn;
    
    @FXML private Button addUserBtn;
    @FXML private Button viewUsersBtn;
    @FXML private Button editUserBtn;
    @FXML private Button deleteUserBtn;
    @FXML private Button generateReportsBtn;
    @FXML private Button systemSettingsBtn;
    
    @FXML private Label userLabel;
    
    // Instance of your business logic classes
    private UserManagement userManagement;
    private SubjectManagement subjectManagement;
    private GradeApproval gradeApproval;
    private Admin currentAdmin;
    
    @FXML
    public void initialize() {
        // Initialize your management classes
        userManagement = new UserManagement();
        subjectManagement = new SubjectManagement();
        gradeApproval = new GradeApproval();
        
        // Set user info (you'll pass this when loading the scene)
        // userLabel.setText(currentAdmin.getName());
    }
    
    // Navigation button handlers
    @FXML
    private void handleUserManagement(ActionEvent event) {
        // Switch to user management view
        System.out.println("User Management clicked");
    }
    
    @FXML
    private void handleSubjectManagement(ActionEvent event) {
        // Switch to subject management view
        System.out.println("Subject Management clicked");
    }
    
    @FXML
    private void handleGradeApproval(ActionEvent event) {
        // Switch to grade approval view
        System.out.println("Grade Approval clicked");
    }
    
    // Action button handlers
    @FXML
    private void handleAddUser(ActionEvent event) {
        // Call UserManagement to add user
        userManagement.AddUser();
    }
    
    @FXML
    private void SearchById(ActionEvent event) {
        // Call UserManagement to view all users
        userManagement.SearchById();
    }
    
    @FXML
    private void UpdateUser(ActionEvent event) {
        // Call UserManagement to edit user
        userManagement.UpdateUser();
    }
    
    @FXML
    private void handleDeleteUser(ActionEvent event) {
        // Call UserManagement to delete user
        userManagement.DeleteUser();
    }
    
    @FXML
    private void handleGenerateReports(ActionEvent event) {
        // Generate reports logic
        System.out.println("Generate Reports clicked");
    }
    
    @FXML
    private void handleSystemSettings(ActionEvent event) {
        // System settings logic
        System.out.println("System Settings clicked");
    }
    
    @FXML
    private void handleLogout(ActionEvent event) {
        // Logout logic - return to login screen
        System.out.println("Logout clicked");
    }
    
    // Method to set the current admin (call this when loading the dashboard)
    public void setCurrentAdmin(Admin admin) {
        this.currentAdmin = admin;
        userLabel.setText(admin.GetName());
    }
}