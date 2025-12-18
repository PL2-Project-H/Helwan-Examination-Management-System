package lecturer;

import model.Exam;
import model.Question;
import model.MultipleChoiceQuestion;
import model.TrueFalseQuestion;
import model.ShortAnswerQuestion;
import util.FileHandler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ExamManagement {

    private static final String EXAMS_FILE = "exams.txt";
    private static final String QUESTIONS_FILE = "questions.txt";

    public List<Exam> getAllExams() {
        List<String> lines = FileHandler.readAllLines(EXAMS_FILE);
        return lines.stream()
                .map(this::parseExam)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public List<Exam> getExamsByLecturerId(String lecturerId) {
        return getAllExams().stream()
                .filter(exam -> exam.getLecturerId().equals(lecturerId))
                .collect(Collectors.toList());
    }

    public void addExam(Exam exam) {
        List<Exam> exams = getAllExams();
        String newId = String.valueOf(exams.stream()
                .map(e -> Integer.parseInt(e.getExamId()))
                .max(Integer::compare)
                .orElse(0) + 1);
        exam.setExamId(newId);
        exams.add(exam);
        saveExams(exams);
    }

    public void updateExam(Exam examToUpdate) {
        List<Exam> exams = getAllExams();
        for (int i = 0; i < exams.size(); i++) {
            if (exams.get(i).getExamId().equals(examToUpdate.getExamId())) {
                exams.set(i, examToUpdate);
                break;
            }
        }
        saveExams(exams);
    }

    public void deleteExam(String examId) {
        List<Exam> exams = getAllExams();
        exams.removeIf(e -> e.getExamId().equals(examId));
        saveExams(exams);


        List<Question> questions = getAllQuestions();
        questions.removeIf(q -> q.getExamId().equals(examId));
        saveQuestions(questions);
    }

    private void saveExams(List<Exam> exams) {
        List<String> lines = exams.stream()
                .map(this::formatExam)
                .collect(Collectors.toList());
        FileHandler.writeAllLines(EXAMS_FILE, lines);
    }

    private Optional<Exam> parseExam(String line) {
        String[] parts = line.split(",");
        if (parts.length == 5) {
            try {
                return Optional.of(new Exam(parts[0], parts[1], parts[2], parts[3], Integer.parseInt(parts[4])));
            } catch (NumberFormatException e) {
                System.err.println("Error parsing exam line: " + line + " - " + e.getMessage());
            }
        }
        return Optional.empty();
    }

    private String formatExam(Exam exam) {
        return String.join(",",
                exam.getExamId(),
                exam.getSubjectId(),
                exam.getLecturerId(),
                exam.getTitle(),
                String.valueOf(exam.getDurationInMinutes()));
    }


    public List<Question> getAllQuestions() {
        List<String> lines = FileHandler.readAllLines(QUESTIONS_FILE);
        return lines.stream()
                .map(this::parseQuestion)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public List<Question> getQuestionsByExamId(String examId) {
        return getAllQuestions().stream()
                .filter(q -> q.getExamId().equals(examId))
                .collect(Collectors.toList());
    }
    
    public void addQuestion(Question question) {
        List<Question> questions = getAllQuestions();
        String newId = String.valueOf(questions.stream()
                .map(q -> Integer.parseInt(q.getQuestionId()))
                .max(Integer::compare)
                .orElse(0) + 1);
        question.setQuestionId(newId);
        questions.add(question);
        saveQuestions(questions);
    }

    public void updateQuestion(Question questionToUpdate) {
        List<Question> questions = getAllQuestions();
        for (int i = 0; i < questions.size(); i++) {
            if (questions.get(i).getQuestionId().equals(questionToUpdate.getQuestionId())) {
                questions.set(i, questionToUpdate);
                break;
            }
        }
        saveQuestions(questions);
    }

    public void deleteQuestion(String questionId) {
        List<Question> questions = getAllQuestions();
        questions.removeIf(q -> q.getQuestionId().equals(questionId));
        saveQuestions(questions);
    }

    private void saveQuestions(List<Question> questions) {
        List<String> lines = questions.stream()
                .map(this::formatQuestion)
                .collect(Collectors.toList());
        FileHandler.writeAllLines(QUESTIONS_FILE, lines);
    }

    private Optional<Question> parseQuestion(String line) {
        String[] parts = line.split("\\|", 3);
        if (parts.length < 3) return Optional.empty();

        String[] meta = parts[0].split(",");
        if (meta.length != 3) return Optional.empty();

        String qId = meta[0];
        String eId = meta[1];
        String type = meta[2];
        String text = parts[1];
        String data = parts[2];

        try {
            switch (type) {
                case "MultipleChoice":
                    String[] mcParts = data.split("\\|");
                    List<String> options = Arrays.asList(mcParts[0].split(";"));
                    int correctIndex = Integer.parseInt(mcParts[1]);
                    return Optional.of(new MultipleChoiceQuestion(qId, eId, text, options, correctIndex));
                case "TrueFalse":
                    return Optional.of(new TrueFalseQuestion(qId, eId, text, Boolean.parseBoolean(data)));
                case "ShortAnswer":
                    return Optional.of(new ShortAnswerQuestion(qId, eId, text, data));
            }
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.err.println("Error parsing question data: " + line + " - " + e.getMessage());
        }
        return Optional.empty();
    }

    private String formatQuestion(Question q) {
        String meta = String.join(",", q.getQuestionId(), q.getExamId(), q.getType());
        String text = q.getQuestionText();
        String data = "";

        if (q instanceof MultipleChoiceQuestion) {
            MultipleChoiceQuestion mcq = (MultipleChoiceQuestion) q;
            String options = String.join(";", mcq.getOptions());
            data = options + "|" + mcq.getCorrectOptionIndex();
        } else if (q instanceof TrueFalseQuestion) {
            data = String.valueOf(((TrueFalseQuestion) q).isCorrectAnswer());
        } else if (q instanceof ShortAnswerQuestion) {
            data = ((ShortAnswerQuestion) q).getCorrectAnswer();
        }
        return meta + "|" + text + "|" + data;
    }
}
