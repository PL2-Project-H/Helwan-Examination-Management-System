package admin;
import java.io.*;
import java.util.*;



public class SubjectManagement {
    private static final File FILE = new File("../data/subject.txt");
    private List<Subject> subjects = new ArrayList<>();
    
    public SubjectManagement() {
        ReadSubjects(); 
    }
    public void ReadSubjects() {
    try {
        Scanner fs = new Scanner(FILE);
        while (fs.hasNextLine()) {
            String line = fs.nextLine().trim();
            if (!line.isEmpty()) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    subjects.add(new Subject(parts[0].trim(), parts[1].trim()));
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
            for (Subject s : subjects) {
                pw.println(s.code + "," + s.name);
            }
            pw.close();
        } catch (Exception e) {
            System.out.println("Users' writing error" + e.getMessage());
        }
    }
    public void AddSubject(String code, String name) {
        for (Subject s : subjects) {
            if (s.GetCode().equals(code)) {
                System.out.println("Subject already exists " + code);
                return;
            }
        }
        subjects.add(new Subject(code, name));
        WriteToFile();
    }
     public void DeleteSubject (String code) {
        boolean removed = false;
        for (int i = 0; i < subjects.size(); i++) {
            Subject s = subjects.get(i);
            if (s.GetCode().equals(code)) {
                subjects.remove(i);  
                removed = true;
                break;            
        }
    }
        if (removed) {
            WriteToFile();
            System.out.println("Subject deleted" + code);
        } else {
            System.out.println("Subject not found" + code);
        }
    }
    public void SearchByCode(String code) {
        for (Subject s : subjects) {
            if (s.GetCode().equals(code)) {
                System.out.println("Found Code:" + s.code + ", Name=" + s.name);
                return;
            }
        }
        System.out.println("Not Found"+ code);
    }
}