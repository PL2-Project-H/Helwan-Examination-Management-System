package admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Grade;

public class GradeApprovalController {

    @FXML
    private TableView<Grade> gradesTable;

    @FXML
    private TableColumn<Grade, String> gradeIdColumn;

    @FXML
    private TableColumn<Grade, String> examIdColumn;

    @FXML
    private TableColumn<Grade, String> studentIdColumn;

    @FXML
    private TableColumn<Grade, Double> scoreColumn;

    @FXML
    private TableColumn<Grade, Boolean> approvedColumn;

    private GradeApproval gradeApproval;

    public void initialize() {
        gradeApproval = new GradeApproval();
        gradeIdColumn.setCellValueFactory(new PropertyValueFactory<>("gradeId"));
        examIdColumn.setCellValueFactory(new PropertyValueFactory<>("examId"));
        studentIdColumn.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        approvedColumn.setCellValueFactory(new PropertyValueFactory<>("approved"));
        loadGrades();
    }

    private void loadGrades() {
        ObservableList<Grade> grades = FXCollections.observableArrayList(gradeApproval.getAllGrades());
        gradesTable.setItems(grades);
    }

    @FXML
    private void handleApproveGrade() {
        Grade selectedGrade = gradesTable.getSelectionModel().getSelectedItem();
        if (selectedGrade != null) {
            gradeApproval.approveGrade(selectedGrade.getGradeId());
            loadGrades();
        }
    }
}
