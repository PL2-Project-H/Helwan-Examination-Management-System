package model;

public class Grade {
    private String gradeId;
    private String examId;
    private String studentId;
    private double score;
    private boolean isApproved;

    public Grade(String gradeId, String examId, String studentId, double score, boolean isApproved) {
        this.gradeId = gradeId;
        this.examId = examId;
        this.studentId = studentId;
        this.score = score;
        this.isApproved = isApproved;
    }

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }
}
