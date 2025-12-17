package user;

import util.FileHandler;
import util.Validator;
import java.util.List;
import java.util.ArrayList;

public class UpdateProfile {

    private static final String USERS_FILE = "users.txt";

    /**
     * Update user's username
     */
    public static boolean updateUsername(String userId, String newUsername) {
        if (!Validator.isValidUsername(newUsername)) {
            return false;
        }

        List<User> users = getAllUsers();
        boolean updated = false;

        for (User user : users) {
            if (user.getId().equals(userId)) {
                user.setUsername(newUsername);
                updated = true;
                break;
            }
        }

        if (updated) {
            saveAllUsers(users);
        }

        return updated;
    }

    /**
     * Update user's password
     */
    public static boolean updatePassword(String userId, String currentPassword, String newPassword) {
        if (!Validator.isValidPassword(newPassword)) {
            return false;
        }

        List<User> users = getAllUsers();
        boolean updated = false;

        for (User user : users) {
            if (user.getId().equals(userId) && user.getPassword().equals(currentPassword)) {
                user.setPassword(newPassword);
                updated = true;
                break;
            }
        }

        if (updated) {
            saveAllUsers(users);
        }

        return updated;
    }

    /**
     * Get user by ID
     */
    public static User getUserById(String userId) {
        List<User> users = getAllUsers();
        for (User user : users) {
            if (user.getId().equals(userId)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Get all users from file
     */
    private static List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        List<String> lines = FileHandler.readAllLines(USERS_FILE);

        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length == 4) {
                try {
                    String id = parts[0];
                    String username = parts[1];
                    String password = parts[2];
                    User.Role role = User.Role.valueOf(parts[3]);
                    users.add(new User(id, username, password, role));
                } catch (IllegalArgumentException e) {
                    System.err.println("Error parsing user line: " + line);
                }
            }
        }

        return users;
    }

    /**
     * Save all users to file
     */
    private static void saveAllUsers(List<User> users) {
        List<String> lines = new ArrayList<>();
        for (User user : users) {
            lines.add(String.join(",",
                    user.getId(),
                    user.getUsername(),
                    user.getPassword(),
                    user.getRole().name()));
        }
        FileHandler.writeAllLines(USERS_FILE, lines);
    }
}
