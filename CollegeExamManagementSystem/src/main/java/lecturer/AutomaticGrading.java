package lecturer;

import model.Question;
import model.MultipleChoiceQuestion;
import model.TrueFalseQuestion;
import model.ShortAnswerQuestion;
import model.Grade;
import util.FileHandler;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class AutomaticGrading {

    private static final String GRADES_FILE = "grades.txt";
    private static final String STUDENT_ANSWERS_FILE = "student_answers.txt";
    
    private ExamManagement examManagement;

    public AutomaticGrading() {
        this.examManagement = new ExamManagement();
    }

    /**
     * 
     * @param studentId
     * @param examId
     * @param studentAnswers
     * @return
     */
    public double calculateAndSaveGrade(String studentId, String examId, Map<String, String> studentAnswers) {
        List<Question> questions = examManagement.getQuestionsByExamId(examId);
        
        if (questions.isEmpty()) {
            return 0.0;
        }

        int totalQuestions = questions.size();
        int correctAnswers = 0;

    
        for (Question question : questions) {
            String studentAnswer = studentAnswers.get(question.getQuestionId());
            if (studentAnswer != null && isAnswerCorrect(question, studentAnswer)) {
                correctAnswers++;
            }
        }

    
        double score = (double) correctAnswers / totalQuestions * 100.0;
        score = Math.round(score * 100.0) / 100.0; // Round to 2 decimal places

        // Save grade to file
        saveGrade(studentId, examId, score);

        return score;
    }


    private boolean isAnswerCorrect(Question question, String studentAnswer) {
        if (studentAnswer == null || studentAnswer.trim().isEmpty()) {
            return false;
        }

        if (question instanceof MultipleChoiceQuestion) {
            MultipleChoiceQuestion mcq = (MultipleChoiceQuestion) question;
            try {
                int studentIndex = Integer.parseInt(studentAnswer.trim());
                return studentIndex == mcq.getCorrectOptionIndex();
            } catch (NumberFormatException e) {
                return false;
            }
        } else if (question instanceof TrueFalseQuestion) {
            TrueFalseQuestion tfq = (TrueFalseQuestion) question;
            boolean studentBoolAnswer = Boolean.parseBoolean(studentAnswer.trim());
            return studentBoolAnswer == tfq.isCorrectAnswer();
        } else if (question instanceof ShortAnswerQuestion) {
            ShortAnswerQuestion saq = (ShortAnswerQuestion) question;
        
            return studentAnswer.trim().equalsIgnoreCase(saq.getCorrectAnswer().trim());
        }

        return false;
    }


    private void saveGrade(String studentId, String examId, double score) {
        List<Grade> grades = getAllGrades();
        

        int maxId = 0;
        for (Grade g : grades) {
            try {
                int id = Integer.parseInt(g.getGradeId());
                if (id > maxId) {
                    maxId = id;
                }
            } catch (NumberFormatException e) {
            
            }
        }
        String newId = String.valueOf(maxId + 1);


        Grade newGrade = new Grade(newId, examId, studentId, score, false);
        grades.add(newGrade);

        
        List<String> lines = new ArrayList<>();
        for (Grade grade : grades) {
            lines.add(formatGrade(grade));
        }
        FileHandler.writeAllLines(GRADES_FILE, lines);
    }


    private List<Grade> getAllGrades() {
        List<Grade> grades = new ArrayList<>();
        List<String> lines = FileHandler.readAllLines(GRADES_FILE);
        
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length == 5) {
                try {
                    String gradeId = parts[0];
                    String examId = parts[1];
                    String studentId = parts[2];
                    double score = Double.parseDouble(parts[3]);
                    boolean isApproved = Boolean.parseBoolean(parts[4]);
                    grades.add(new Grade(gradeId, examId, studentId, score, isApproved));
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing grade line: " + line);
                }
            }
        }
        
        return grades;
    }


    private String formatGrade(Grade grade) {
        return String.join(",",
                grade.getGradeId(),
                grade.getExamId(),
                grade.getStudentId(),
                String.valueOf(grade.getScore()),
                String.valueOf(grade.isApproved()));
    }


    public Grade getGrade(String studentId, String examId) {
        List<Grade> grades = getAllGrades();
        for (Grade grade : grades) {
            if (grade.getStudentId().equals(studentId) && grade.getExamId().equals(examId)) {
                return grade;
            }
        }
        return null;
    }
}
