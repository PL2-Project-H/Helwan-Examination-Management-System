package admin;

import model.Grade;
import util.FileHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GradeApproval {

    private static final String GRADES_FILE = "grades.txt";

    public List<Grade> getAllGrades() {
    List<Grade> grades = new ArrayList<>();
    List<String> lines = FileHandler.readAllLines(GRADES_FILE);

    for (String line : lines) {
        Optional<Grade> optionalGrade = parseGrade(line);
        if (optionalGrade.isPresent()) {
            grades.add(optionalGrade.get());
        }
    }

    return grades;
}


    public void approveGrade(String gradeId) {
        List<Grade> grades = getAllGrades();
        for (int i = 0; i < grades.size(); i++) {
            if (grades.get(i).getGradeId().equals(gradeId)) {
                grades.get(i).setApproved(true);
                break;
            }
        }
        saveGrades(grades);
    }

    private void saveGrades(List<Grade> grades) {
    List<String> lines = new ArrayList<>();

    for (Grade grade : grades) {
        lines.add(formatGrade(grade));
    }

    FileHandler.writeAllLines(GRADES_FILE, lines);
}


    private Optional<Grade> parseGrade(String line) {
        String[] parts = line.split(",");
        if (parts.length == 5) {
            try {
                String gradeId = parts[0];
                String examId = parts[1];
                String studentId = parts[2];
                double score = Double.parseDouble(parts[3]);
                boolean isApproved = Boolean.parseBoolean(parts[4]);
                return Optional.of(new Grade(gradeId, examId, studentId, score, isApproved));
            } catch (IllegalArgumentException e) {
                System.err.println("Error parsing grade line: " + line + " - " + e.getMessage());
            }
        }
        return Optional.empty();
    }

    private String formatGrade(Grade grade) {
        return String.join(",",
                grade.getGradeId(),
                grade.getExamId(),
                grade.getStudentId(),
                String.valueOf(grade.getScore()),
                String.valueOf(grade.isApproved()));
    }
}
