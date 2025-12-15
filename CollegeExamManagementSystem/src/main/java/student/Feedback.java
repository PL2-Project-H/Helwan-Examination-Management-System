package student;

import util.FileHandler;
import user.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class Feedback {

    private static final String FEEDBACK_FILE = "feedback.txt";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void submitFeedback(String studentId, String feedbackText) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String feedbackEntry = String.join(",", studentId, timestamp, feedbackText.replace(",", ";;;")); // Replace commas in feedback to avoid parsing issues
        FileHandler.appendLine(FEEDBACK_FILE, feedbackEntry);
    }

    public List<FeedbackEntry> getAllFeedback() {
        List<String> lines = FileHandler.readAllLines(FEEDBACK_FILE);
        return lines.stream()
                .map(this::parseFeedbackEntry)
                .filter(java.util.Optional::isPresent)
                .map(java.util.Optional::get)
                .collect(Collectors.toList());
    }

    private java.util.Optional<FeedbackEntry> parseFeedbackEntry(String line) {
        String[] parts = line.split(",", 3); // Limit to 3 parts to handle commas in feedback text
        if (parts.length == 3) {
            try {
                String studentId = parts[0];
                String timestamp = parts[1];
                String feedbackText = parts[2].replace(";;;", ","); // Restore commas
                return java.util.Optional.of(new FeedbackEntry(studentId, timestamp, feedbackText));
            } catch (Exception e) {
                System.err.println("Error parsing feedback line: " + line + " - " + e.getMessage());
            }
        }
        return java.util.Optional.empty();
    }

    public static class FeedbackEntry {
        private String studentId;
        private String timestamp;
        private String feedbackText;

        public FeedbackEntry(String studentId, String timestamp, String feedbackText) {
            this.studentId = studentId;
            this.timestamp = timestamp;
            this.feedbackText = feedbackText;
        }

        public String getStudentId() {
            return studentId;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public String getFeedbackText() {
            return feedbackText;
        }
    }
}
