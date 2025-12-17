package admin;

import user.User;
import util.FileHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserManagement {

    private static final String USERS_FILE = "users.txt";

    public List<User> getAllUsers() {
    List<User> users = new ArrayList<>();
    List<String> lines = FileHandler.readAllLines(USERS_FILE);

    for (String line : lines) {
        Optional<User> optionalUser = parseUser(line);
        if (optionalUser.isPresent()) {
            users.add(optionalUser.get());
        }
    }

    return users;
}


    public List<User> getManageableUsers() {
    List<User> allUsers = getAllUsers();
    List<User> manageableUsers = new ArrayList<>();

    for (User user : allUsers) {
        if (user.getRole() != User.Role.ADMIN) {
            manageableUsers.add(user);
        }
    }

    return manageableUsers;
}


    public void addUser(User user) {
    List<User> users = getAllUsers();

    int maxId = 0;
    for (User u : users) {
        try {
            int id = Integer.parseInt(u.getId());
            if (id > maxId) {
                maxId = id;
            }
        } catch (NumberFormatException e) {
        }
    }

    String newId = String.valueOf(maxId + 1);
    user.setId(newId);

    users.add(user);
    saveUsers(users);
}


    public void updateUser(User userToUpdate) {
        List<User> users = getAllUsers();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(userToUpdate.getId())) {
                users.set(i, userToUpdate);
                break;
            }
        }
        saveUsers(users);
    }
    public User searchById(String userId) {
        List<User> users = getAllUsers();

        for (User user : users) {
            if (user.getId().equals(userId)) {
                return user;
            }
        }

        return null; 
    }

    public void deleteUser(String userId) {
        List<User> users = getAllUsers();
        users.removeIf(user -> user.getId().equals(userId));
        saveUsers(users);
    }

    private void saveUsers(List<User> users) {
    List<String> lines = new ArrayList<>();

    for (User user : users) {
        lines.add(formatUser(user));
    }

    FileHandler.writeAllLines(USERS_FILE, lines);
    }


    private Optional<User> parseUser(String line) {
        String[] parts = line.split(",");
        if (parts.length == 4) {
            try {
                String id = parts[0];
                String username = parts[1];
                String password = parts[2];
                User.Role role = User.Role.valueOf(parts[3]);
                return Optional.of(new User(id, username, password, role));
            } catch (IllegalArgumentException e) {
                System.err.println("Error parsing user line: " + line + " - " + e.getMessage());
            }
        }
        return Optional.empty();
    }

    private String formatUser(User user) {
        return String.join(",",
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getRole().name());
    }
}
