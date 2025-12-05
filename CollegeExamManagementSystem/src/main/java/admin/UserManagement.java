package Admin;
import java.io.*;
import java.util.*;

public class UserManagement{
    private static final File FILE = new File("../data/users.txt");
    private List<User> users = new ArrayList<>();
    
    public UserManagement() {
        ReadUsers(); 
    }
    public void SetName(String name, User user) {
        user.name = name;
    }
    public void SetId(String id, User user) {
        user.id = id;
    }
    public void SetPasswd(String pass, User user) {
        user.pass = pass;
    }
    public void ReadUsers() {
    try {
        Scanner fs = new Scanner(FILE);
        while (fs.hasNextLine()) {
            String line = fs.nextLine().trim();
            if (!line.isEmpty()) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    users.add(new User(parts[0].trim(), parts[1].trim(), parts[2].trim()));
                }
            }
        }
        fs.close();
    } catch (Exception e) {
        System.out.println(FILE + " Not Found");
    }
    }
      private void WriteToFile() {
        try {
            PrintWriter pw = new PrintWriter(FILE);
            for (User u : users) {
                pw.println(u.id + "," + u.name + "," + u.pass); 
            }
            pw.close();
        } catch (Exception e) {
            System.out.println("Users' writing error" + e.getMessage());
        }
    }
     public void AddUser(String id, String name, String pass) {
        for (User u : users) {
            if (u.GetId().equals(id)) {
                System.out.println("User already exists" + id);
                return;
            }
        }
        users.add(new User(id, name, pass));
        WriteToFile();
    }
     public void DeleteUser (String id) {
        boolean removed = false;
        for (int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            if (u.GetId().equals(id)) {
                users.remove(i);  
                removed = true;
                break;            
        }
    }
        if (removed) {
            WriteToFile();
            System.out.println("User deleted" + id);
        } else {
            System.out.println("User not found" + id);
        }
    }
     public void UpdateUser(String id, String newName, String newPass) {
        boolean found = false;
        for (User u : users) {
            if (u.GetId().equals(id)) {
                u.name = newName;
                u.pass = newPass;
                found = true;
                break;
            }
        }
        if (found) WriteToFile();
    }
     public void SearchById(String id) {
        for (User u : users) {
            if (u.GetId().equals(id)) {
                System.out.println("Found Id:" + u.id + ", Name=" + u.name);
                return;
            }
        }
        System.out.println("Not Found"+ id);
    }
}