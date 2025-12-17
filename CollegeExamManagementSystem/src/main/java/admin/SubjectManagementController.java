package admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Subject;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import user.User;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class SubjectManagementController {

    @FXML
    private TableView<Subject> subjectsTable;

    @FXML
    private TableColumn<Subject, String> idColumn;

    @FXML
    private TableColumn<Subject, String> nameColumn;

    private SubjectManagement subjectManagement;

    public void initialize() {
        subjectManagement = new SubjectManagement();
        idColumn.setCellValueFactory(new PropertyValueFactory<>("subjectId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("subjectName"));
        loadSubjects();
    }

    private void loadSubjects() {
        ObservableList<Subject> subjects = FXCollections.observableArrayList(subjectManagement.getAllSubjects());
        subjectsTable.setItems(subjects);
    }

    @FXML
    private void handleAddSubject() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Subject");
        dialog.setHeaderText("Enter Subject Name");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            if (!name.isEmpty()) {
                subjectManagement.addSubject(new Subject(null, name));
                loadSubjects();
            }
        });
    }

    @FXML
    private void handleEditSubject() {
        Subject selectedSubject = subjectsTable.getSelectionModel().getSelectedItem();
        if (selectedSubject != null) {
            TextInputDialog dialog = new TextInputDialog(selectedSubject.getSubjectName());
            dialog.setTitle("Edit Subject");
            dialog.setHeaderText("Enter New Subject Name");
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(name -> {
                if (!name.isEmpty()) {
                    selectedSubject.setSubjectName(name);
                    subjectManagement.updateSubject(selectedSubject);
                    loadSubjects();
                }
            });
        }
    }

    @FXML
    private void handleDeleteSubject() {
        Subject selectedSubject = subjectsTable.getSelectionModel().getSelectedItem();
        if (selectedSubject != null) {
            subjectManagement.deleteSubject(selectedSubject.getSubjectId());
            loadSubjects();
        }
    }

    @FXML
    private void handleAssignToStudents() {
        assignSubjectsToUser(User.Role.STUDENT);
    }

    @FXML
    private void handleAssignToLecturers() {
        assignSubjectsToUser(User.Role.LECTURER);
    }

    private void assignSubjectsToUser(User.Role role) {
        UserManagement userManagement = new UserManagement();
        List<User> users = userManagement.getAllUsers().stream()
                .filter(u -> u.getRole() == role)
                .collect(Collectors.toList());

        if (users.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "No Users", "No " + role.name().toLowerCase() + "s found in the system.");
            return;
        }

        // Step 1: Select user
        ChoiceDialog<User> userDialog = new ChoiceDialog<>(users.get(0), users);
        userDialog.setTitle("Assign Subjects");
        userDialog.setHeaderText("Select " + role.name());
        userDialog.setContentText("Choose a " + role.name().toLowerCase() + ":");
        
        // Custom converter to show username
        userDialog.getItems().clear();
        userDialog.getItems().addAll(users);
        
        Optional<User> userResult = userDialog.showAndWait();
        if (!userResult.isPresent()) return;

        User selectedUser = userResult.get();

        // Step 2: Select subjects (multi-select)
        List<Subject> allSubjects = subjectManagement.getAllSubjects();
        List<Subject> currentlyAssigned = subjectManagement.getSubjectsByUserId(selectedUser.getId(), role);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Assign Subjects");
        alert.setHeaderText("Assign subjects to " + selectedUser.getUsername());

        ListView<Subject> listView = new ListView<>();
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listView.setItems(FXCollections.observableArrayList(allSubjects));
        
        // Pre-select currently assigned subjects
        for (Subject subject : currentlyAssigned) {
            listView.getSelectionModel().select(subject);
        }

        VBox content = new VBox(10);
        content.getChildren().addAll(
            new Label("Select subjects (use Ctrl+Click for multiple):"),
            listView
        );
        alert.getDialogPane().setContent(content);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            List<Subject> selectedSubjects = new ArrayList<>(listView.getSelectionModel().getSelectedItems());
            subjectManagement.updateAssignments(selectedUser.getId(), role, selectedSubjects);
            showAlert(Alert.AlertType.INFORMATION, "Success", 
                "Subjects assigned successfully to " + selectedUser.getUsername());
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
