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

    public String GetId() {
        return id;
    }

    public void SetId(String id) {
        this.id = id;
    }

    public String GetName() {
        return name;
    }

    public void SetName(String name) {
        this.name = name;
    }

    public String SetPass() {
        return pass;
    }

    public void SetPass(String pass) {
        this.pass = pass;
    }

    public Role GetRole() {
        return role;
    }

    public void SetRole(Role role) {
        this.role = role;
    }
}
