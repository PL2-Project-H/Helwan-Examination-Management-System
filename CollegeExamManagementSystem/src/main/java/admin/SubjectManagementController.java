package admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Subject;
import javafx.scene.control.TextInputDialog;
import java.util.Optional;

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
}
