package lecturer;

import model.Exam;
import model.Grade;
import admin.GradeApproval;
import user.User;
import admin.UserManagement;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Reporting {

    private ExamManagement examManagement;
    private GradeApproval gradeApproval;
    private UserManagement userManagement;

    public Reporting() {
        this.examManagement = new ExamManagement();
        this.gradeApproval = new GradeApproval();
        this.userManagement = new UserManagement();
    }

    public List<Exam> getExamsByLecturer(String lecturerId) {
        return examManagement.getExamsByLecturerId(lecturerId);
    }

    public List<GradeReportEntry> getGradesForExam(String examId) {
        List<Grade> grades = gradeApproval.getAllGrades().stream()
                                .filter(g -> g.getExamId().equals(examId))
                                .collect(Collectors.toList());
        
        List<GradeReportEntry> reportEntries = new ArrayList<>();
        for (Grade grade : grades) {
            User student = userManagement.getAllUsers().stream()
                                .filter(u -> u.getId().equals(grade.getStudentId()))
                                .findFirst().orElse(null);
            
            String studentName = (student != null) ? student.getUsername() : "Unknown Student";
            reportEntries.add(new GradeReportEntry(grade.getGradeId(), studentName, grade.getScore(), grade.isApproved()));
        }
        return reportEntries;
    }

    public static class GradeReportEntry {
        private String gradeId;
        private String studentName;
        private double score;
        private boolean isApproved;

        public GradeReportEntry(String gradeId, String studentName, double score, boolean isApproved) {
            this.gradeId = gradeId;
            this.studentName = studentName;
            this.score = score;
            this.isApproved = isApproved;
        }

        public String getGradeId() { return gradeId; }
        public String getStudentName() { return studentName; }
        public double getScore() { return score; }
        public boolean isApproved() { return isApproved; }
    }
}
