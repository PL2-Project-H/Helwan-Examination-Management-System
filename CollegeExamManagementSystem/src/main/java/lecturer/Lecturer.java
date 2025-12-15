package lecturer;

import user.User;

public class Lecturer extends User {
    public Lecturer(String id, String username, String password) {
        super(id, username, password, Role.LECTURER);
    }
}
