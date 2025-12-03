import java.util.*;

class User {

    protected String name;
    protected String id;
    protected String pass;
    public User (String id, String name, String pass) {
        this.name = name;
        this.pass = pass;
        this.id = id;
    }
    public String getId() {
        return this.id;
    }
    public String getName() {
        return this.name;
    }
}