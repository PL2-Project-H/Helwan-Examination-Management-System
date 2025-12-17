package admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import user.User;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ChoiceDialog;
import java.util.Optional;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UserManagementController {

    @FXML
    private TableView<User> usersTable;

    @FXML
    private TableColumn<User, String> idColumn;

    @FXML
    private TableColumn<User, String> usernameColumn;

    @FXML
    private TableColumn<User, String> passwordColumn;

    @FXML
    private TableColumn<User, User.Role> roleColumn;

    private UserManagement userManagement;

    public void initialize() {
        userManagement = new UserManagement();
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        loadUsers();
    }

    private void loadUsers() {
        ObservableList<User> users = FXCollections.observableArrayList(userManagement.getAllUsers());
        usersTable.setItems(users);
    }

    @FXML
    private void handleAddUser() {
        TextInputDialog usernameDialog = new TextInputDialog();
        usernameDialog.setTitle("Add User");
        usernameDialog.setHeaderText("Enter Username");
        Optional<String> usernameResult = usernameDialog.showAndWait();

        if (usernameResult.isPresent() && !usernameResult.get().isEmpty()) {
            List<User.Role> roles = Arrays.stream(User.Role.values()).collect(Collectors.toList());
            ChoiceDialog<User.Role> roleDialog = new ChoiceDialog<>(User.Role.STUDENT, roles);
            roleDialog.setTitle("Add User");
            roleDialog.setHeaderText("Select Role");
            Optional<User.Role> roleResult = roleDialog.showAndWait();

            if (roleResult.isPresent()) {
                User newUser = new User(null, usernameResult.get(), "password", roleResult.get());
                userManagement.addUser(newUser);
                loadUsers();
            }
        }
    }

    @FXML
    private void handleEditUser() {
        User selectedUser = usersTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            TextInputDialog usernameDialog = new TextInputDialog(selectedUser.getUsername());
            TextInputDialog passwordDialog = new TextInputDialog(selectedUser.getPassword());
            usernameDialog.setTitle("Edit User");
            usernameDialog.setHeaderText("Enter Username");
            passwordDialog.setHeaderText("Enter Password");
            Optional<String> usernameResult = usernameDialog.showAndWait();
            Optional<String> passwordResult = passwordDialog.showAndWait();

            if (usernameResult.isPresent() && !usernameResult.get().isEmpty()) {
                List<User.Role> roles = Arrays.stream(User.Role.values()).collect(Collectors.toList());
                ChoiceDialog<User.Role> roleDialog = new ChoiceDialog<>(selectedUser.getRole(), roles);
                roleDialog.setTitle("Edit User");
                roleDialog.setHeaderText("Select Role");
                Optional<User.Role> roleResult = roleDialog.showAndWait();

                if (roleResult.isPresent()) {
                    selectedUser.setUsername(usernameResult.get());
                    selectedUser.setRole(roleResult.get());
                    selectedUser.setPassword(passwordResult.get());
                    userManagement.updateUser(selectedUser);
                    loadUsers();
                }
            }
        }
    }

    @FXML
    private void handleDeleteUser() {
        User selectedUser = usersTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            userManagement.deleteUser(selectedUser.getId());
            loadUsers();
        }
    }
    @FXML
        private void handleSearchUser() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Search User");
        dialog.setHeaderText("Enter User ID");
        Optional<String> result = dialog.showAndWait();

        if (result.isPresent() && !result.get().isEmpty()) {
            User user = userManagement.searchById(result.get());

            if (user != null) {

                ObservableList<User> singleUser = FXCollections.observableArrayList(user);
                usersTable.setItems(singleUser);
            } else 
                System.out.println("User not found!");
            }
        }
}