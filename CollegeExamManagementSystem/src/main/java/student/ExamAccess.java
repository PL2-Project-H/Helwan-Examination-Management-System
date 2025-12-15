package student;

import model.Exam;
import lecturer.ExamManagement;
import model.Question;
import util.FileHandler;
import user.User;
import admin.SubjectManagement;
import model.Subject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ExamAccess {

    private static final String STUDENT_ATTEMPTS_FILE = "student_attempts.txt";
    private static final String STUDENT_ANSWERS_FILE = "student_answers.txt";
    private static final String GRADES_FILE = "grades.txt"; // For checking if graded

    private ExamManagement examManagement;
    private SubjectManagement subjectManagement;

    public ExamAccess() {
        this.examManagement = new ExamManagement();
        this.subjectManagement = new SubjectManagement();
    }

    public List<Exam> getAvailableExamsForStudent(String studentId) {
        List<Subject> studentSubjects = subjectManagement.getSubjectsByUserId(studentId, User.Role.STUDENT);
        List<String> studentSubjectIds = studentSubjects.stream()
                                            .map(Subject::getSubjectId)
                                            .collect(Collectors.toList());

        return examManagement.getAllExams().stream()
                .filter(exam -> studentSubjectIds.contains(exam.getSubjectId()))
                .collect(Collectors.toList());
    }

    public boolean hasAttemptedExam(String studentId, String examId) {
        List<String> lines = FileHandler.readAllLines(STUDENT_ATTEMPTS_FILE);
        return lines.stream()
                .anyMatch(line -> {
                    String[] parts = line.split(",");
                    return parts.length == 2 && parts[0].equals(studentId) && parts[1].equals(examId);
                });
    }

    public void recordAttempt(String studentId, String examId) {
        FileHandler.appendLine(STUDENT_ATTEMPTS_FILE, studentId + "," + examId);
    }

    public void saveAnswers(String studentId, String examId, Map<String, String> answers) {
        List<String> linesToAppend = new ArrayList<>();
        for (Map.Entry<String, String> entry : answers.entrySet()) {
            linesToAppend.add(String.join(",", studentId, examId, entry.getKey(), entry.getValue()));
        }
        FileHandler.writeAllLines(STUDENT_ANSWERS_FILE, linesToAppend); // Overwrite for simplicity, or append carefully
    }

    public Map<String, String> getStudentAnswers(String studentId, String examId) {
        Map<String, String> answers = new HashMap<>();
        List<String> lines = FileHandler.readAllLines(STUDENT_ANSWERS_FILE);
        lines.stream()
             .filter(line -> {
                 String[] parts = line.split(",", 4);
                 return parts.length == 4 && parts[0].equals(studentId) && parts[1].equals(examId);
             })
             .forEach(line -> {
                 String[] parts = line.split(",", 4);
                 answers.put(parts[2], parts[3]);
             });
        return answers;
    }

    public List<Question> getQuestionsForExam(String examId) {
        return examManagement.getQuestionsByExamId(examId);
    }
}
