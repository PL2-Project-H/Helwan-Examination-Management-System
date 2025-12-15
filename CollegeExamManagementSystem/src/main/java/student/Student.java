package student;

import user.User;

public class Student extends User {
    public Student(String id, String username, String password) {
        super(id, username, password, Role.STUDENT);
    }
}
