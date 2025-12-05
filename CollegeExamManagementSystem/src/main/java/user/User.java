package user;

public class User {
    public enum Role { ADMIN, LECTURER, STUDENT }

    public String id;
    public String name;
    public String pass;
    public Role role;

    public User(String id, String name, String pass) {
        this(id, name, pass, Role.STUDENT);
    }

    public User(String id, String name, String pass, Role role) {
        this.id = id;
        this.name = name;
        this.pass = pass;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String username) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String password) {
        this.pass = pass;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
