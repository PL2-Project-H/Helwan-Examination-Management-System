package admin;

import model.Subject;
import user.User;
import util.FileHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SubjectManagement {

    private static final String SUBJECTS_FILE = "subjects.txt";
    private static final String STUDENT_SUBJECTS_FILE = "student_subjects.txt";
    private static final String LECTURER_SUBJECTS_FILE = "lecturer_subjects.txt";


    public List<Subject> getAllSubjects() {
    List<Subject> subjects = new ArrayList<>();
    List<String> lines = FileHandler.readAllLines(SUBJECTS_FILE);

    for (String line : lines) {
        Optional<Subject> optionalSubject = parseSubject(line);
        if (optionalSubject.isPresent()) {
            subjects.add(optionalSubject.get());
        }
    }

    return subjects;
}


    public void addSubject(Subject subject) {
    List<Subject> subjects = getAllSubjects();

    int maxId = 100;
    for (Subject s : subjects) {
        try {
            int id = Integer.parseInt(s.getSubjectId());
            if (id > maxId) {
                maxId = id;
            }
        } catch (NumberFormatException e) {
        }
    }

    String newId = String.valueOf(maxId + 1);
    subject.setSubjectId(newId);

    subjects.add(subject);
    saveSubjects(subjects);
}


    public void updateSubject(Subject subjectToUpdate) {
        List<Subject> subjects = getAllSubjects();
        for (int i = 0; i < subjects.size(); i++) {
            if (subjects.get(i).getSubjectId().equals(subjectToUpdate.getSubjectId())) {
                subjects.set(i, subjectToUpdate);
                break;
            }
        }
        saveSubjects(subjects);
    }

    public void deleteSubject(String subjectId) {
        List<Subject> subjects = getAllSubjects();
        subjects.removeIf(subject -> subject.getSubjectId().equals(subjectId));
        saveSubjects(subjects);

        removeAssignmentsForSubject(subjectId, STUDENT_SUBJECTS_FILE);
        removeAssignmentsForSubject(subjectId, LECTURER_SUBJECTS_FILE);
    }

    private void saveSubjects(List<Subject> subjects) {
    List<String> lines = new ArrayList<>();

    for (Subject subject : subjects) {
        lines.add(formatSubject(subject));
    }

    FileHandler.writeAllLines(SUBJECTS_FILE, lines);
}


    private Optional<Subject> parseSubject(String line) {
        String[] parts = line.split(",");
        if (parts.length == 2) {
            return Optional.of(new Subject(parts[0], parts[1]));
        }
        return Optional.empty();
    }

    private String formatSubject(Subject subject) {
        return String.join(",",
                subject.getSubjectId(),
                subject.getSubjectName());
    }

    public List<Subject> getSubjectsByUserId(String userId, User.Role role) {
    String assignmentFile =
            (role == User.Role.STUDENT) ? STUDENT_SUBJECTS_FILE : LECTURER_SUBJECTS_FILE;

    List<String> assignmentLines = FileHandler.readAllLines(assignmentFile);
    List<String> assignedSubjectIds = new ArrayList<>();

    for (String line : assignmentLines) {
        if (line.startsWith(userId + ",")) {
            String[] parts = line.split(",");
            if (parts.length == 2) {
                assignedSubjectIds.add(parts[1]);
            }
        }
    }

    List<Subject> result = new ArrayList<>();
    List<Subject> allSubjects = getAllSubjects();

    for (Subject subject : allSubjects) {
        if (assignedSubjectIds.contains(subject.getSubjectId())) {
            result.add(subject);
        }
    }

    return result;
}


    public void updateAssignments(String userId, User.Role role, List<Subject> newAssignedSubjects) {
    String assignmentFile =
            (role == User.Role.STUDENT) ? STUDENT_SUBJECTS_FILE : LECTURER_SUBJECTS_FILE;

    List<String> allAssignmentLines = FileHandler.readAllLines(assignmentFile);
    List<String> filteredAssignments = new ArrayList<>();

   
    for (String line : allAssignmentLines) {
        if (!line.startsWith(userId + ",")) {
            filteredAssignments.add(line);
        }
    }

    for (Subject subject : newAssignedSubjects) {
        filteredAssignments.add(userId + "," + subject.getSubjectId());
    }

    FileHandler.writeAllLines(assignmentFile, filteredAssignments);
}


    private void removeAssignmentsForSubject(String subjectId, String assignmentFile) {
    List<String> allAssignmentLines = FileHandler.readAllLines(assignmentFile);
    List<String> filteredAssignments = new ArrayList<>();

    for (String line : allAssignmentLines) {
        if (!line.endsWith("," + subjectId)) {
            filteredAssignments.add(line);
        }
    }

    FileHandler.writeAllLines(assignmentFile, filteredAssignments);
}

}
