package UserManagement;

public class Admin extends User {

    protected Admin (String name, String id, String pass) {
        super(name,id,pass);
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