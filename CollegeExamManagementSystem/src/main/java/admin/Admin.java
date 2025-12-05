package admin;
import user.User;

public class Admin extends User {

    public Admin(String name, String id, String pass) {
        super(id, name, pass, Role.ADMIN); 
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setPasswd(String pass) {
        this.pass = pass;
    }
}

