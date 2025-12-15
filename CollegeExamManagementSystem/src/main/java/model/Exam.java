package model;

public class Exam {
    private String examId;
    private String subjectId;
    private String lecturerId;
    private String title;
    private int durationInMinutes;

    public Exam(String examId, String subjectId, String lecturerId, String title, int durationInMinutes) {
        this.examId = examId;
        this.subjectId = subjectId;
        this.lecturerId = lecturerId;
        this.title = title;
        this.durationInMinutes = durationInMinutes;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getLecturerId() {
        return lecturerId;
    }

    public void setLecturerId(String lecturerId) {
        this.lecturerId = lecturerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(int durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }
}
